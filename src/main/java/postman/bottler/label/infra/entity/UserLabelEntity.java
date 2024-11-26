package postman.bottler.label.infra.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import postman.bottler.user.infra.entity.UserEntity;

@Entity
@Table(name = "user_label")
public class UserLabelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userLabelId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "label_id", nullable = false)
    private LabelEntity label;
}
