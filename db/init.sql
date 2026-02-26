CREATE TABLE medications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name   VARCHAR(255) NOT NULL,
    dosage VARCHAR(255) NOT NULL
);

INSERT INTO medications (name, dosage) VALUES
    ('Aspirin',     '100mg'),
    ('Metformin',   '500mg'),
    ('Lisinopril',  '10mg'),
    ('Amoxicillin', '250mg'),
    ('Omeprazole',  '20mg');
