package com.petitionvoice.backend.seeder;

import com.petitionvoice.backend.enums.PetitionState;
import com.petitionvoice.backend.enums.PetitionCategory;
import com.petitionvoice.backend.enums.Role;
import com.petitionvoice.backend.model.AddedPetition;
import com.petitionvoice.backend.model.User;
import com.petitionvoice.backend.model.UserDetails;
import com.petitionvoice.backend.repository.PetitionRepository;
import com.petitionvoice.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class DbSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PetitionRepository petitionRepository;
    private final PasswordEncoder passwordEncoder;

    public DbSeeder(UserRepository userRepository, PetitionRepository petitionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.petitionRepository = petitionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        //Seeder user
        if(userRepository.count() == 0) {
            List<User> users = new ArrayList<>();

            User u1 = new User();
            u1.setFirst_name("Larisa");
            u1.setLast_name("Lisei");
            u1.setRole(Role.REGISTERED_USER);

            User u2 = new User();
            u2.setFirst_name("Sorin");
            u2.setLast_name("Albu");
            u2.setRole(Role.REGISTERED_USER);

            User u3 = new User();
            u3.setFirst_name("Andrei");
            u3.setLast_name("Duda");
            u3.setRole(Role.REGISTERED_USER);

            User u4 = new User();
            u4.setFirst_name("Pavel");
            u4.setLast_name("Glavan");
            u4.setRole(Role.REGISTERED_USER);

            users.add(u1);
            users.add(u2);
            users.add(u3);
            users.add(u4);

            //user details
            UserDetails ud1 = new UserDetails();
            ud1.setUser(u1);
            ud1.setEmail("larisa_lisei@gmail.com");
            ud1.setTelephone("0759373029");
            ud1.setPassword_hash(passwordEncoder.encode("larisa"));

            u1.setUserDetails(ud1);

            UserDetails ud2 = new UserDetails();
            ud2.setUser(u2);
            ud2.setEmail("sorin_albu@gmail.com");
            ud2.setTelephone("0758271345");
            ud2.setPassword_hash(passwordEncoder.encode("sorin"));

            u2.setUserDetails(ud2);

            UserDetails ud3 = new UserDetails();
            ud3.setUser(u3);
            ud3.setEmail("andrei_duda@gmail.com");
            ud3.setTelephone("0759028167");
            ud3.setPassword_hash(passwordEncoder.encode("andrei"));

            u3.setUserDetails(ud3);

            UserDetails ud4 = new UserDetails();
            ud4.setUser(u4);
            ud4.setEmail("pavel_glavan@gmail.com");
            ud4.setTelephone("0757232019");
            ud4.setPassword_hash(passwordEncoder.encode("pavel"));

            u4.setUserDetails(ud4);

            userRepository.saveAll(users);

        }

        //seeder petition
        if (petitionRepository.count() == 0) {

            List<User> users = userRepository.findAll();
            List<AddedPetition> petitions = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            String[][] petitionData = new String[][]{
                    {"Save the local park", "ENVIRONMENT", "Protect the park from construction.", "01-01-2025", "01-03-2026", "1000", "4", "/uploads/petitions/env5.jpg"},
                    {"Support animal shelter", "ANIMALS", "Help fund our local animal shelter.", "02-01-2025", "01-04-2026", "500", "2", "/uploads/petitions/animals1.jpg"},
                    {"Improve students mental health", "EDUCATION", "Remove final studies exam", "03-01-2025", "01-05-2026", "300", "3", "/uploads/petitions/ed5.webp"},
                    {"Healthcare funding increase", "HEALTHCARE", "Increase funding for public hospitals.", "04-01-2025", "01-06-2025", "1500", "1", "/uploads/petitions/health1.jpg"},
                    {"Protect cultural heritage", "CULTURE", "Preserve historical buildings.", "05-01-2025", "15-06-2026", "800", "2", "/uploads/petitions/culture1.jpg"},
                    {"Human Rights Awareness", "HUMAN_RIGHTS", "Promote human rights education.", "06-01-2025", "01-07-2026", "600", "4", "/uploads/petitions/hr2.jpg"},
                    {"Clean the river", "ENVIRONMENT", "Stop pollution in our local river.", "07-01-2025", "15-07-2026", "700", "1", "/uploads/petitions/env2.jpg"},
                    {"Reduce plastic usage", "ENVIRONMENT", "Encourage local businesses to reduce plastics.", "08-01-2025", "01-08-2026", "900", "2", "/uploads/petitions/env3.jpg"},
                    {"Support local artists", "CULTURE", "Funding for local art programs.", "09-01-2025", "15-08-2026", "400", "3", "/uploads/petitions/culture2.webp"},
                    {"Protect endangered species", "ANIMALS", "Save endangered animals from extinction.", "10-01-2025", "01-09-2026", "1200", "1", "/uploads/petitions/animals2.jpg"},
                    {"Equal pay campaign", "HUMAN_RIGHTS", "Promote gender pay equality.", "11-01-2025", "15-09-2026", "1000", "2", "/uploads/petitions/hr1.jpg"},
                    {"Plant 1000 trees", "ENVIRONMENT", "Increase green cover in city.", "13-01-2025", "15-10-2025", "1000", "1", "/uploads/petitions/env6.jpg"},
                    {"Support mental health", "HEALTHCARE", "Funding for mental health programs.", "14-01-2025", "01-11-2026", "800", "4", "/uploads/petitions/health2.jpg"},
                    {"Reduce noise pollution", "ENVIRONMENT", "Limit noise in residential areas.", "15-01-2025", "15-11-2026", "600", "3", "/uploads/petitions/env1.jpg"},
                    {"Promote recycling", "ENVIRONMENT", "Encourage household recycling.", "16-01-2025", "01-12-2026", "900", "1", "/uploads/petitions/env3.jpg"},
                    {"Save local library", "EDUCATION", "Prevent closure of local library.", "17-01-2025", "15-12-2026", "400", "2", "/uploads/petitions/ed2.jpg"},
                    {"Healthcare accessibility", "HEALTHCARE", "Make healthcare more accessible.", "18-01-2025", "01-01-2026", "1200", "3", "/uploads/petitions/health6.webp"},
                    {"Protect wetlands", "ENVIRONMENT", "Preserve wetlands near the city.", "19-01-2025", "15-01-2026", "700", "4", "/uploads/petitions/env1.jpg"},
                    {"Animal adoption campaign", "ANIMALS", "Encourage adoption of shelter pets.", "20-01-2025", "01-02-2026", "500", "2", "/uploads/petitions/animals3.webp"},
                    {"NO EXAMS", "EDUCATION", "Encourage students mental health", "21-01-2025", "15-02-2026", "600", "3", "/uploads/petitions/ed3.webp"},
                    {"Food for the homeless", "HUMAN_RIGHTS", "Provide meals to homeless people.", "22-01-2025", "01-03-2026", "800", "1", "/uploads/petitions/hr10.webp"},
                    {"Clean the beach", "ENVIRONMENT", "Organize a beach cleanup campaign.", "23-01-2025", "15-03-2026", "400", "2", "/uploads/petitions/env8.jpg"},
                    {"Protect rainforest", "ENVIRONMENT", "Save local rainforest areas.", "24-01-2025", "01-04-2026", "1000", "3", "/uploads/petitions/env9.webp"},
                    {"Art in public spaces", "CULTURE", "Install murals and art in public areas.", "24-01-2025", "15-04-2026", "300", "1", "/uploads/petitions/culture3.jpg"},
                    {"Vaccination awareness", "HEALTHCARE", "Promote vaccination campaigns.", "26-01-2025", "01-05-2026", "1200", "4", "/uploads/petitions/health5.svg"},
                    {"Save endangered birds", "ANIMALS", "Protect endangered bird species.", "27-01-2025", "15-05-2026", "600", "3", "/uploads/petitions/animals4.jpg"},
                    {"Promote reading habits", "EDUCATION", "Encourage children to read more.", "28-01-2025", "01-06-2026", "500", "1", "/uploads/petitions/ed4.jpg"},
                    {"Clean the city park", "ENVIRONMENT", "Remove litter and trash from park.", "29-01-2025", "15-06-2026", "400", "4", "/uploads/petitions/env2.jpg"},
                    {"Support local musicians", "CULTURE", "Funding for music events.", "30-01-2025", "01-07-2026", "300", "3", "/uploads/petitions/culture4.webp"},
                    {"Protect rivers", "ENVIRONMENT", "Prevent pollution in rivers.", "31-01-2025", "15-07-2026", "700", "1", "/uploads/petitions/env3.jpg"},
                    {"Mental health for students", "HEALTHCARE", "Provide counseling in schools.", "01-02-2025", "01-08-2026", "500", "2", "/uploads/petitions/health3.jpg"},
                    {"Support animal rights", "ANIMALS", "Campaign against animal cruelty.", "03-02-2025", "01-09-2026", "600", "1", "/uploads/petitions/animals11.jpg"},
                    {"Cultural festival", "CULTURE", "Organize annual cultural festival.", "04-02-2025", "15-09-2026", "300", "2", "/uploads/petitions/culture1.jpg"},
                    {"Education for all", "EDUCATION", "Promote access to education.", "05-02-2025", "01-10-2026", "1200", "4", "/uploads/petitions/ed1.jpg"},
                    {"Clean air initiative", "ENVIRONMENT", "Reduce air pollution in city.", "06-02-2025", "15-10-2026", "800", "1", "/uploads/petitions/env7.jpg"},
                    {"Support local theater", "CULTURE", "Funding for theater programs.", "07-02-2025", "01-11-2026", "400", "2", "/uploads/petitions/culture8.jpg"},
                    {"Healthcare for elderly", "HEALTHCARE", "Improve healthcare for seniors.", "08-02-2025", "15-11-2026", "1000", "3", "/uploads/petitions/health4.jpg"},
                    {"Animal rescue program", "ANIMALS", "Support animal rescue operations.", "09-02-2025", "01-12-2026", "500", "4", "/uploads/petitions/animals10.jpg"},
                    {"School lunch improvements", "EDUCATION", "Provide healthy meals for students.", "10-02-2025", "15-12-2026", "700", "2", "/uploads/petitions/ed6.jpg"},
                    {"Protect national parks", "ENVIRONMENT", "Preserve natural parks.", "11-02-2025", "01-01-2027", "900", "3", "/uploads/petitions/env5.jpg"},
                    {"Promote public libraries", "EDUCATION", "Increase funding for public libraries.", "12-02-2025", "15-01-2027", "400", "1", "/uploads/petitions/ed7.webp"},
                    {"Clean neighborhood streets", "ENVIRONMENT", "Organize street cleaning events.", "13-02-2025", "01-02-2027", "300", "4", "/uploads/petitions/env1.jpg"},
                    {"Support local museums", "CULTURE", "Funding for museum programs.", "14-02-2025", "15-02-2027", "200", "3", "/uploads/petitions/culture1.jpg"},
                    {"Promote local crafts", "CULTURE", "Support local handmade products.", "17-02-2025", "01-04-2027", "300", "3", "/uploads/petitions/culture2.webp"},
                    {"Food safety awareness", "HEALTHCARE", "Educate public about food safety.", "18-02-2025", "15-04-2027", "400", "1", "/uploads/petitions/health2.jpg"},
                    {"Animal welfare week", "ANIMALS", "Promote animal welfare campaigns.", "19-02-2025", "01-05-2027", "500", "2", "/uploads/petitions/animals13.jpg"},
                    {"School renovation program", "EDUCATION", "Renovate old schools.", "20-02-2025", "15-05-2027", "1000", "3", "/uploads/petitions/ed8.jpg"},
                    {"Community garden initiative", "ENVIRONMENT", "Create community gardens.", "21-02-2025", "01-06-2027", "300", "4", "/uploads/petitions/env11.jpg"},
                    {"Improve hospital facilities", "HEALTHCARE", "Upgrade hospital equipment.", "23-02-2025", "01-07-2027", "1200", "3", "/uploads/petitions/health7.jpg"},
                    {"Protect endangered forests", "ENVIRONMENT", "Preserve forests from deforestation.", "04-02-2025", "15-07-2027", "1000", "1", "/uploads/petitions/env9.webp"},
                    {"Promote clean energy", "ENVIRONMENT", "Support clean energy initiatives.", "25-02-2025", "01-08-2027", "900", "4", "/uploads/petitions/env12.jpg"},
                    {"Support arts education", "EDUCATION", "Funding for arts in schools.", "26-02-2025", "15-08-2027", "400", "3", "/uploads/petitions/ed9.jpg"},
                    {"Mental health awareness", "HEALTHCARE", "Raise awareness about mental health.", "27-02-2025", "01-09-2027", "600", "1", "/uploads/petitions/health8.jpg"},
                    {"Animal protection laws", "ANIMALS", "Advocate for stricter animal laws.", "28-02-2025", "15-09-2027", "700", "2", "/uploads/petitions/animals12.jpg"},
                    {"Healthcare awareness campaign", "HEALTHCARE", "Educate about preventive care.", "04-03-2025", "15-11-2027", "500", "3", "/uploads/petitions/health9.jpg"},
                    {"Protect endangered humans", "ANIMALS", "We are under a big threat. We have to unite against our common enemy. The ducks.", "05-03-2025", "01-12-2027", "400", "1", "/uploads/petitions/animals14.jpg"},
                    {"Support education for girls", "EDUCATION", "Promote education access for girls.", "06-03-2025", "15-12-2027", "700", "2", "/uploads/petitions/ed10.jpg"},
                    {"Clean city parks initiative", "ENVIRONMENT", "Organize clean-up events in parks.", "07-03-2025", "01-01-2028", "600", "4", "/uploads/petitions/env13.jpg"},
                    {"Healthcare funding for children", "HEALTHCARE", "Improve child healthcare funding.", "09-03-2025", "01-02-2028", "900", "4", "/uploads/petitions/health10.jpg"},
                    {"Animal rescue volunteers", "ANIMALS", "Encourage volunteering for rescues.", "10-03-2025", "15-02-2028", "500", "3", "/uploads/petitions/animals15.webp"},
                    {"All we want is peace", "HUMAN_RIGHTS", "One last fight for peace.", "22-01-2025", "01-03-2026", "800", "1", "/uploads/petitions/hr9.webp"}
            };

            for (String[] row : petitionData) {
                AddedPetition petition = new AddedPetition();
                petition.setTitle(row[0]);
                petition.setCategory(row[1]);
                petition.setDescription(row[2]);
                petition.setCreation_date(sdf.parse(row[3]));
                petition.setExpiration_date(sdf.parse(row[4]));
                petition.setGoal(Integer.parseInt(row[5]));
                petition.setCount(0);
                petition.setFeedback("");
                petition.setState(PetitionState.APPROVED);
                int userIndex = Integer.parseInt(row[6]) - 1; //index in list starts from 0
                petition.setCreator(users.get(userIndex));
                petition.setImageUrl(row[7]);

                petitions.add(petition);
            }

            petitionRepository.saveAll(petitions);
        }
    }
}
