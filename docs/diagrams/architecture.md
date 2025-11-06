# Architecture Diagram

This diagram shows the Clean Architecture layers and their dependencies.

> **Note:** `FacultyMenu` creates Hibernate service implementations directly (dashed lines), which creates a coupling between Presentation and Infrastructure layers. The ABM classes depend on Service interfaces (solid lines), maintaining proper abstraction. In a production environment, this coupling could be resolved using Dependency Injection.

```mermaid
graph TB
    subgraph "Presentation Layer"
        A[FacultyMenu]
        B[CityABM]
        C[FacultyABM]
        D[CareerABM]
        E[ProfessorABM]
        F[SubjectABM]
        G[StudentABM]
    end
    
    subgraph "Application Layer"
        H[CityService]
        I[FacultyService]
        J[CareerService]
        K[ProfessorService]
        L[SubjectService]
        M[StudentService]
    end
    
    subgraph "Infrastructure Layer"
        N[BaseHibernateService]
        O[HibernateCityService]
        P[HibernateFacultyService]
        Q[HibernateCareerService]
        R[HibernateProfessorService]
        S[HibernateSubjectService]
        T[HibernateStudentService]
    end
    
    subgraph "Domain Layer"
        U[Model Entities]
        V[HibernateUtil]
    end
    
    A -.creates with.-> O
    A -.creates with.-> P
    A -.creates with.-> Q
    A -.creates with.-> R
    A -.creates with.-> S
    A -.creates with.-> T
    
    A --> B
    A --> C
    A --> D
    A --> E
    A --> F
    A --> G
    
    B --> H
    C --> I
    D --> J
    E --> K
    F --> L
    G --> M
    
    H --> O
    I --> P
    J --> Q
    K --> R
    L --> S
    M --> T
    
    O --> N
    P --> N
    Q --> N
    R --> N
    S --> N
    T --> N
    
    N --> V
    N --> U
    V --> U
    
    style A fill:#e1f5ff
    style O fill:#fff4e1
    style P fill:#fff4e1
    style Q fill:#fff4e1
    style R fill:#fff4e1
    style S fill:#fff4e1
    style T fill:#fff4e1
```

