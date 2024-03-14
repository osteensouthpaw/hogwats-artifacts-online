package com.omega.hogwatsartifactsonline.artifacts;

import com.omega.hogwatsartifactsonline.wizards.Wizard;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Artifact implements Serializable {
    @Id
    private String artifactId;
    private String name;
    private String description;
    private String imageUrl;

    @ManyToOne
    private Wizard owner;
}
