package postman.bottler.label.infra.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import postman.bottler.user.infra.entity.UserEntity;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_label")
public class UserLabelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userLabelId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Cascade(CascadeType.PERSIST)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "label_id", nullable = false)
    @Cascade(CascadeType.PERSIST)
    private LabelEntity label;

    public static UserLabelEntity from(UserEntity userEntity, LabelEntity labelEntity) {
        return UserLabelEntity.builder()
                .user(userEntity)
                .label(labelEntity)
                .build();
    }
}
