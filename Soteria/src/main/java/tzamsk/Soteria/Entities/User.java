package tzamsk.Soteria.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 64, nullable = false)
    private String username;
    @Column(length = 128, nullable = false, unique = true)
    private String email;
    @Column(length = 128, nullable = false)
    private String password;
    private String profile;
    private boolean active;
    private boolean banned;

    public User(){}

    public User(String username, String email){
        this.username = username;
        this.email = email;
    }
    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public User(int id, String username, String email, String password){
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public User(int id, String username, String email, String password, String profile, boolean active, boolean banned){
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.active = active;
        this.banned = banned;
    }

    public void setId(Integer id){ this.id = id; }
    public Integer getId(){ return id; }

    public void setUsername(String username){ this.username = username; }
    public String getUsername(){ return username; }

    public void setEmail(String email){ this.email = email; }
    public String getEmail(){ return email; }

    public void setPassword(String password){ this.password = password; }
    public String getPassword(){ return password; }

    public void setProfile(String profile){ this.profile = profile; }
    public String getProfile(){ return profile; }

    public void isActive(boolean active){ this.active = active; }
    public boolean getActive(){ return active; }

    public void isBanned(boolean banned){ this.banned = banned; }
    public boolean getBanned(){ return banned; }

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet();

    public void setRoles(Set<Role> roles){ this.roles = roles; }
    public Set<Role> getRoles(){ return roles; }
    public void add(Role role){ this.roles.add(role); }
}
