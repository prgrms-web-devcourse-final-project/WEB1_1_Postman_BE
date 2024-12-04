package postman.bottler.batch;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;
import postman.bottler.letter.service.LetterBoxRepository;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.NotificationType;
import postman.bottler.notification.domain.PushMessages;
import postman.bottler.notification.domain.Subscriptions;
import postman.bottler.notification.service.NotificationRepository;
import postman.bottler.notification.service.PushNotificationProvider;
import postman.bottler.notification.service.SubscriptionRepository;
import postman.bottler.user.infra.UserJpaRepository;
import postman.bottler.user.infra.entity.UserEntity;

@Configuration
@RequiredArgsConstructor
public class KeywordBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final UserJpaRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PushNotificationProvider provider;
    private final NotificationRepository notificationRepository;
    private final LetterBoxRepository letterBoxRepository;

    @Bean
    public Job recommendKeywordBatchJob() {
        return new JobBuilder("recommendKeywordBatchJob", jobRepository)
                .start(recommendKeywordBatchStep())
                .build();
    }

    @Bean
    public Step recommendKeywordBatchStep() {
        return new StepBuilder("recommendKeywordBatchStep", jobRepository)
                .<UserEntity, Notification>chunk(10, platformTransactionManager)
                .reader(userReader())
                .processor(recommendKeywordProcessor())
                .writer(recommendKeywordWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<UserEntity> userReader() {
        return new RepositoryItemReaderBuilder<UserEntity>()
                .name("userReader")
                .pageSize(10)
                .methodName("findAll")
                .repository(userRepository)
                .sorts(Map.of("userId", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<UserEntity, Notification> recommendKeywordProcessor() {
        return new ItemProcessor<UserEntity, Notification>() {
            @Override
            public Notification process(UserEntity user) throws Exception {
                Long letterId = 1L; // TODO 키워드 편지 추천
                Subscriptions subscriptions = subscriptionRepository.findByUserId(user.getUserId());
                PushMessages pushMessages = subscriptions.makeMessages(NotificationType.NEW_LETTER);
                if (subscriptions.isPushEnabled()) {
                    provider.pushAll(pushMessages);
                }
                return Notification.create("NEW_LETTER", user.getUserId(), letterId);
            }
        };
    }

    @Bean
    public ItemWriter<Notification> recommendKeywordWriter() {
        return new NotificationWriter(notificationRepository, letterBoxRepository);
    }
}
