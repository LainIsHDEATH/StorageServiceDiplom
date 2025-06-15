package diplom.work.storageservice.model.simulation;

import diplom.work.storageservice.model.room.Room;
import diplom.work.storageservice.model.sensor_data.SensorData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "simulations")
@Getter
@Setter
@NoArgsConstructor
public class Simulation implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "controller_type")
    private ControllerType controllerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.CREATED;

    @Column(name = "iterations")
    private Long iterations;

    @Column(name = "timestep_seconds")
    private Integer timestepSeconds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "simulation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SensorData> events = new ArrayList<>();

    public enum Status { CREATED, RUNNING, FINISHED, FAILED }
    public enum ControllerType { PID, PID_LSTM, RL, TRAIN_RL, AUTOTUNE_PID }
}
