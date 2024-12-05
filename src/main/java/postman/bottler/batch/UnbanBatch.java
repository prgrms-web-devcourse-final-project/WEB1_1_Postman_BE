package postman.bottler.batch;

import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import postman.bottler.user.domain.User;
import postman.bottler.user.infra.JpaBanRepository;
import postman.bottler.user.infra.entity.BanEntity;
import postman.bottler.user.service.UserRepository;

@Configuration
@RequiredArgsConstructor
public class UnbanBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final UserRepository userRepository;
    private final JpaBanRepository banRepository;

    @Bean
    public Job unbanJob() {
        return new JobBuilder("unbanJob", jobRepository)
                .start(unbanStep())
                .build();
    }

    @Bean
    public Step unbanStep() {
        return new StepBuilder("unban", jobRepository)
                .<BanEntity, User>chunk(10, platformTransactionManager)
                .reader(banReader(null))
                .processor(unbanProcessor())
                .writer(unbanWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<BanEntity> banReader(@Value("#{jobParameters[now]}") String localDateTime) {
        return new JpaPagingItemReaderBuilder<BanEntity>()
                .name("banReader")
                .pageSize(10)
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from BanEntity b where unbansAt <= :now order by userId")
                .parameterValues(Map.of("now", LocalDateTime.parse(localDateTime)))
                .build();
    }

    @Bean
    public ItemProcessor<BanEntity, User> unbanProcessor() {
        return ban -> {
            User user = userRepository.findById(ban.getUserId());
            user.unban();
            return user;
        };
    }

    @Bean
    public ItemWriter<User> unbanWriter() {
        return new ItemWriter<User>() {
            @Override
            public void write(Chunk<? extends User> chunk) throws Exception {
                List<? extends User> items = chunk.getItems();
                List<Long> ids = items.stream().map(User::getUserId)
                        .toList();
                banRepository.deleteByIds(ids);
                userRepository.unbanUsers(ids);
            }
        };
    }
}
