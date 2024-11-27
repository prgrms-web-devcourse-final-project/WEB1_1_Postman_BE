package postman.bottler.label.infra.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import postman.bottler.label.domain.Label;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "label")
public class LabelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long labelId;

    @Column(unique = true, nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int limitCount;

    @Column(nullable = false)
    @Builder.Default
    private int ownedCount = 0;

    public static LabelEntity from(Label label) {
        return LabelEntity.builder()
                .labelId(label.getLabelId())
                .imageUrl(label.getImageUrl())
                .limitCount(label.getLimitCount())
                .build();
    }

    public static List<Label> toLabels(List<LabelEntity> entities) {
        return entities.stream()
                .map(LabelEntity::to)
                .toList();
    }

    public static Label toLabel(LabelEntity labelEntity) {
        return labelEntity.to();
    }

    public Label to() {
        return Label.createLabel(this.labelId, this.imageUrl, this.limitCount, this.ownedCount);
    }

    public void updateOwnedCount() {
        this.ownedCount++;
    }
}
