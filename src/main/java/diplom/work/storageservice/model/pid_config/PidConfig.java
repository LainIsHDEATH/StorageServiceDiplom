package diplom.work.storageservice.model.pid_config;

import diplom.work.storageservice.model.room.Room;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pid_configs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PidConfig {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kp", nullable = false)
    private double kp;

    @Column(name = "ki", nullable = false)
    private double ki;

    @Column(name = "kd", nullable = false)
    private double kd;

    @Column(name = "tuned_method", length = 32)
    private String tunedMethod;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "is_active")
    private boolean active = true;
}