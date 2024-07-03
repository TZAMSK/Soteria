package tzamsk.Soteria.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 40, nullable = false, unique = true)
    private String name;
    private String description;

    public Role(){}
    public Role(Integer id){
        this.id = id;
    }
    public Role(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void setId(Integer id){ this.id = id; }
    public Integer getId(){ return id; }

    public void setName(String name){ this.name = name; }
    public String getName(){ return name; }

    public void setDescription(String description){ this.description = description; }
    public String getDescription(){ return description; }
}
