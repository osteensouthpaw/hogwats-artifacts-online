package com.omega.hogwatsartifactsonline.wizards;

import com.omega.hogwatsartifactsonline.artifacts.Artifact;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private String id;
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
}
