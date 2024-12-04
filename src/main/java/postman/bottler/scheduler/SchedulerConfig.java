package postman.bottler.scheduler;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Scheduled(cron = "0 0 0 * * *")
    public void sendRecommendKeywordLetter()
            throws NoSuchJobException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("recommendKeywordBatchJob"), jobParameters);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void unban()
            throws NoSuchJobException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        LocalDateTime now = LocalDateTime.now();
        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("now", now)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("unbanJob"), jobParameters);
    }
}
