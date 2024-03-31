package com.omega.hogwatsartifactsonline.db;

import com.omega.hogwatsartifactsonline.artifacts.Artifact;
import com.omega.hogwatsartifactsonline.artifacts.ArtifactRepository;
import com.omega.hogwatsartifactsonline.hogwatsuser.HogwartsUser;
import com.omega.hogwatsartifactsonline.hogwatsuser.UserRepository;
import com.omega.hogwatsartifactsonline.wizards.Wizard;
import com.omega.hogwatsartifactsonline.wizards.WizardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final ArtifactRepository artifactRepository;
    private final WizardRepository wizardRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        Artifact artifact1 = new Artifact();
        artifact1.setArtifactId("1");
        artifact1.setName("Invisibility Cloak");
        artifact1.setDescription("A magical cloak that renders the wearer invisible");
        artifact1.setImageUrl("imageUrl1");

        Artifact artifact2 = new Artifact();
        artifact2.setArtifactId("2");
        artifact2.setName("Marauder's Map");
        artifact2.setDescription("A magical map of Hogwarts that reveals the castle's layout and the location of individuals");
        artifact2.setImageUrl("imageUrl2");

        Artifact artifact3 = new Artifact();
        artifact3.setArtifactId("3");
        artifact3.setName("Philosopher's Stone");
        artifact3.setDescription("Grants immortality and transforms any metal into pure gold");
        artifact3.setImageUrl("imageUrl3");

        Artifact artifact4 = new Artifact();
        artifact4.setArtifactId("4");
        artifact4.setName("Sorting Hat");
        artifact4.setDescription("Magical hat that sorts incoming students at Hogwarts into one of the four houses");
        artifact4.setImageUrl("imageUrl4");

        Artifact artifact5 = new Artifact();
        artifact5.setArtifactId("5");
        artifact5.setName("Time-Turner");
        artifact5.setDescription("Magical device that allows the wearer to travel back in time");
        artifact5.setImageUrl("imageUrl5");

        Artifact artifact6 = new Artifact();
        artifact6.setArtifactId("6");
        artifact6.setName("Elder Wand");
        artifact6.setDescription("One of the three Deathly Hallows; said to be the most powerful wand in existence");
        artifact6.setImageUrl("imageUrl6");

        var wizard = new Wizard();
        wizard.setWizardId(1);
        wizard.setName("harry");
        wizard.addArtifact(artifact1);
        wizard.addArtifact(artifact3);

        var wizard2 = new Wizard();
        wizard2.setWizardId(2);
        wizard2.setName("Neville Longbottom");
        wizard2.addArtifact(artifact2);
        wizard2.addArtifact(artifact4);

        var wizard3 = new Wizard();
        wizard3.setWizardId(3);
        wizard3.setName("Albus");
        wizard3.addArtifact(artifact5);

        wizardRepository.save(wizard);
        wizardRepository.save(wizard2);
        wizardRepository.save(wizard3);

        artifactRepository.save(artifact6);

        //add some users to the database
        HogwartsUser user1 = new HogwartsUser();
        user1.setUserId(1);
        user1.setUsername("omega");
        user1.setPassword("1234");
        user1.setEnabled(true);
        user1.setRoles("admin");


        HogwartsUser user2 = new HogwartsUser();
        user2.setUserId(1);
        user2.setUsername("omega");
        user2.setPassword("1234");
        user2.setEnabled(true);
        user2.setRoles("admin");

        HogwartsUser user3 = new HogwartsUser();
        user3.setUserId(1);
        user3.setUsername("omega");
        user3.setPassword("1234");
        user3.setEnabled(true);
        user3.setRoles("admin");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }
}
