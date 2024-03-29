package com.omega.hogwatsartifactsonline.wizards;

import com.omega.hogwatsartifactsonline.artifacts.Artifact;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Wizard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int wizardId;
    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "owner")
    private List<Artifact> artifacts = new ArrayList<>();

    public void addArtifact(Artifact artifact) {
        artifact.setOwner(this);
        artifacts.add(artifact);
    }

    public int getNumberOfArtifacts() {
        return artifacts.size();
    }

    public void unAssignArtifacts() {
        artifacts.forEach(artifact -> artifact.setOwner(null));
        artifacts.clear();
    }

    public void removeArtifact(Artifact artifact) {
        artifacts.remove(artifact);
        artifact.setOwner(null);
    }
}
