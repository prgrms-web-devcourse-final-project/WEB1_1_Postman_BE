package postman.bottler.label.infra.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import postman.bottler.label.domain.Label;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "label")
public class LabelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long labelId;

    private String imageUrl;

    public static LabelEntity from(Label label) {
        return LabelEntity.builder()
                .imageUrl(label.getImageUrl())
                .build();
    }
}
