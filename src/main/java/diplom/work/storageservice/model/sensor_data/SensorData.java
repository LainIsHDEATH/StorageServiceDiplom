package diplom.work.storageservice.model.sensor_data;

import diplom.work.storageservice.model.room.Room;
import diplom.work.storageservice.model.simulation.Simulation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "simulation_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorData {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simulation_id")
    private Simulation simulation;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "temp_in")
    private Double tempIn;

    @Column(name = "temp_out")
    private Double tempOut;

    @Column(name = "temp_setpoint")
    private Double tempSetpoint;

    @Column(name = "heater_power")
    private Double heaterPower;

    @Column(name = "predicted_temp")
    private Double predictedTemp;

    @Column(name = "is_window_open")
    private Boolean isWindowOpen;

    @Column(name = "is_door_open")
    private Boolean isDoorOpen;

    @Column(name = "people_count")
    private Integer peopleCount;
}
