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

    @Column(unique = true)
    private String imageUrl;

    public static LabelEntity from(Label label) {
        return LabelEntity.builder()
                .imageUrl(label.getImageUrl())
                .build();
    }

    public static List<Label> toLabels(List<LabelEntity> entities) {
        return entities.stream()
                .map(LabelEntity::to)
                .toList();
    }

    public Label to() {
        return Label.createLabel(this.imageUrl);
    }
}