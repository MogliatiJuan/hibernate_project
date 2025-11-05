# Entity Relationship Diagram

This diagram shows the database schema and relationships between entities.

```mermaid
erDiagram
    CIUDAD ||--o{ FACULTAD : "has"
    CIUDAD ||--o{ PROFESOR : "lives_in"
    CIUDAD ||--o{ ALUMNO : "lives_in"
    
    FACULTAD ||--o{ CARRERA : "has"
    
    CARRERA ||--o{ MATERIA : "has"
    
    PROFESOR ||--o{ MATERIA : "teaches"
    
    MATERIA }o--o{ ALUMNO : "enrolled_in"
    
    CIUDAD {
        int idCiudad PK
        string nombre
    }
    
    FACULTAD {
        int idFacultad PK
        string nombre
        int idCiudad FK
    }
    
    CARRERA {
        int idCarrera PK
        string nombre
        int idFacultad FK
    }
    
    PROFESOR {
        int dni PK
        string apellido
        string nombre
        string fechaNac
        int antiguedad
        int idCiudad FK
    }
    
    MATERIA {
        int idMateria PK
        string nombre
        int nivel
        int orden
        int dniProfesor FK
        int idCarrera FK
    }
    
    ALUMNO {
        int dni PK
        string apellido
        string nombre
        date fechaNac
        int numLegajo
        int anioIngreso
        int idCiudad FK
    }
```

