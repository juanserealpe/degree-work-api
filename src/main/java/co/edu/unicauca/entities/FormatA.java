package co.edu.unicauca.entities;

import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.enums.TypeProcess;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "format_a")
@DiscriminatorValue("FORMAT_A")
public class FormatA extends Process {
    @Column(name = "tittle")
    private String title;
    @Column(name = "url")
    private String url;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "format_a_observations", joinColumns = @JoinColumn(name = "format_a_id"))
    @Column(name = "observation")
    @OrderColumn(name = "position") // opcional: conserva el orden de la lista
    private List<String> observations = new ArrayList<>();
    @Column(name = "failed_attempts")
    private byte failedAttempts;

    //Constructors
    public FormatA (){

    }

    //Getters & setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public byte getFailedAttempts() { return failedAttempts; }
    public void setFailedAttempts(byte failedAttempts) { this.failedAttempts = failedAttempts; }

    public List<String> getObservations() { return observations; }
    public void setObservations(List<String> observations) { this.observations = observations; }

}
