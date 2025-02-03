package postman.bottler.scheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.label.application.dto.LabelRequestDTO;
import postman.bottler.label.application.repository.LabelRepository;
import postman.bottler.label.domain.Label;

@Component
@RequiredArgsConstructor
@Slf4j
public class LabelScheduler {
    private final LabelRepository labelRepository;
    private final TaskScheduler taskScheduler;

    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Transactional
    public void scheduleUpdateFirstComeLabel(LabelRequestDTO labelRequestDTO) {
        LocalDateTime scheduledTime = labelRequestDTO.scheduledDateTime();
        List<Long> labelIds = labelRequestDTO.labelIds();

        log.info("{}에 라벨을 FIRST_COME으로 변경하는 스케줄 등록", scheduledTime);

        labelIds.forEach(labelId -> {
            ScheduledFuture<?> scheduledTask = taskScheduler.schedule(() -> {
                log.info("스케줄 실행: 라벨 {}을 FIRST_COME으로 변경", labelId);

                Label label = labelRepository.findLabelByLabelId(labelId);
                label.updateFirstComeLabel();
                labelRepository.save(label);
                scheduledTasks.remove(labelId);

            }, scheduledTime.atZone(ZoneId.systemDefault()).toInstant());

            // 기존에 등록된 스케줄이 있으면 취소하고 새로운 스케줄 등록
            if (scheduledTasks.containsKey(labelId)) {
                scheduledTasks.get(labelId).cancel(false);
                log.info("기존 스케줄 취소: 라벨 {}", labelId);
            }
            scheduledTasks.put(labelId, scheduledTask);
        });
    }
}
