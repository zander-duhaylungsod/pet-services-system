INSERT INTO Services (ServiceName, Price, Description)
VALUES
    ('Full Grooming Package', 1000.00, 'Complete grooming package including bathing, nail trimming, and fur styling.'),
    ('Bathing Only', 300.00, 'Basic cleaning and drying service for pets.'),
    ('Fur Styling', 500.00, 'Custom haircuts and trims based on pet breed and owner preferences.'),
    ('Teeth Cleaning', 400.00, 'Dental hygiene service for pets, ensuring clean teeth and fresh breath.'),
    ('Basic Obedience Training', 1500.00, 'Introductory training to teach pets basic commands like sit, stay, and heel.'),
    ('General Checkup', 800.00, 'Health assessment to ensure the pet is in good condition.'),
    ('Full Package Vaccination', 2000.00, 'Comprehensive vaccination package covering essential pet immunizations.');
--Roles: 'Manager', 'Receptionist', 'Groomer', 'Boarding Attendant'

INSERT INTO Employees (FirstName, LastName, Phone, Role)
VALUES
    ('Zander', 'Duhaylungsod', '09854263832', 'Administrator'),
    ('Nazlah', 'Nanding', '09547812564', 'Administrator'),
    ('John Erold', 'Miano', '09872154654', 'Administrator'),
    ('Jaz Cyrill', 'Samano', '09893154742', 'Administrator'),
    ('Sarah', 'Carpenter', '09171234567', 'Manager'),
    ('Maria', 'Santos', '09172345678', 'Receptionist'),
    ('Pedro', 'Gomez', '09173456789', 'Groomer'),
    ('Liza', 'Reyes', '09174567890', 'Boarding Attendant'),
    ('Roberto', 'Martinez', '09175678901', 'Veterinarian'),
    ('Carmen', 'Dizon', '09176789012', 'Pet Trainer'),
    ('Antonio', 'Garcia', '09177890123', 'Cleaning Staff');
--Predetermined, do not change


-- Populating Owners
INSERT INTO Owners (FirstName, LastName, Email, Phone)
VALUES
    ('Alfredo', 'Cruz', 'alfredo.cruz@example.com', '09071234567'),
    ('Bea', 'Santos', 'bea.santos@example.com', '09081234568'),
    ('Carlos', 'Reyes', 'carlos.reyes@example.com', '09091234569'),
    ('Diana', 'Garcia', 'diana.garcia@example.com', '09101234570'),
    ('Enrique', 'Martinez', 'enrique.martinez@example.com', '09111234571'),
    ('Fely', 'Lopez', 'fely.lopez@example.com', '09121234572'),
    ('George', 'Cruz', 'george.cruz@example.com', '09131234573'),
    ('Helena', 'Ramos', 'helena.ramos@example.com', '09141234574'),
    ('Isabel', 'Torres', 'isabel.torres@example.com', '09151234575'),
    ('Julio', 'Rivera', 'julio.rivera@example.com', '09161234576'),
    ('Karla', 'Gutierrez', 'karla.gutierrez@example.com', '09171234577'),
    ('Luis', 'Domingo', 'luis.domingo@example.com', '09181234578'),
    ('Martha', 'Flores', 'martha.flores@example.com', '09191234579'),
    ('Nico', 'Bautista', 'nico.bautista@example.com', '09201234580'),
    ('Olivia', 'Luna', 'olivia.luna@example.com', '09211234581');

-- Populating Pets
INSERT INTO Pets (Name, Species, Breed, PetImagePath, PetNotes, Age, OwnerID)
VALUES
    ('Bantay', 'Dog', 'Aspin', '/CCE104/petImages/moodeng.jpg', 'Loves guarding the house', 5, 1),
    ('Mingming', 'Cat', 'Siamese', '/CCE104/petImages/moodeng.jpg', 'Very cuddly', 3, 2),
    ('Putol', 'Dog', 'Shih Tzu', '/CCE104/petImages/moodeng.jpg', 'Has a cute bark', 2, 3),
    ('Pusang Kalye', 'Cat', 'Persian', '/CCE104/petImages/moodeng.jpg', 'Loves sleeping all day', 4, 4),
    ('Tagpi', 'Dog', 'Bulldog', '/CCE104/petImages/moodeng.jpg', 'Spots on fur', 6, 5),
    ('Doggiekins', 'Dog', 'Golden Retriever', '/CCE104/petImages/moodeng.jpg', 'Very energetic', 3, 6),
    ('Rover', 'Dog', 'Labrador', '/CCE104/petImages/moodeng.jpg', 'Loves fetching sticks', 4, 7),
    ('Fluffy', 'Cat', 'Maine Coon', '/CCE104/petImages/moodeng.jpg', 'Adores grooming sessions', 2, 8),
    ('Spike', 'Dog', 'Doberman', '/CCE104/petImages/moodeng.jpg', 'Very alert and protective', 4, 9),
    ('Kitty', 'Cat', 'Bengal', '/CCE104/petImages/moodeng.jpg', 'Always curious', 3, 10),
    ('Snowy', 'Dog', 'Husky', '/CCE104/petImages/moodeng.jpg', 'Enjoys winter', 5, 11),
    ('Cuddles', 'Cat', 'British Shorthair', '/CCE104/petImages/moodeng.jpg', 'Loves being held', 6, 12),
    ('Shadow', 'Dog', 'German Shepherd', '/CCE104/petImages/moodeng.jpg', 'Highly trained', 4, 13),
    ('Princess', 'Dog', 'Poodle', '/CCE104/petImages/moodeng.jpg', 'Very graceful', 3, 14),
    ('Tom', 'Cat', 'Russian Blue', '/CCE104/petImages/moodeng.jpg', 'Quiet and calm', 5, 15);

-- Populating Appointments (2021-2024) with updated ServiceID (1-7)
INSERT INTO Appointments (Date, Time, ServiceID, PetID, EmployeeID, Status)
VALUES
    -- 2021 Appointments
    ('2021-02-15', '09:00:00', 1, 1, 1001, 'Completed'),
    ('2021-03-20', '10:00:00', 2, 2, 1002, 'Pending'),
    ('2021-05-12', '11:00:00', 3, 3, 1003, 'Completed'),
    ('2021-06-25', '14:00:00', 4, 4, 1004, 'Canceled'),
    ('2021-07-14', '15:00:00', 5, 5, 1005, 'Completed'),

    -- 2022 Appointments
    ('2022-02-15', '09:00:00', 6, 6, 1006, 'Completed'),
    ('2022-03-20', '10:00:00', 7, 7, 1001, 'Pending'),
    ('2022-05-12', '11:00:00', 1, 8, 1002, 'Completed'),
    ('2022-06-25', '14:00:00', 2, 9, 1003, 'Canceled'),
    ('2022-07-14', '15:00:00', 3, 10, 1004, 'Completed'),

    -- 2023 Appointments
    ('2023-02-15', '09:00:00', 4, 11, 1005, 'Completed'),
    ('2023-03-20', '10:00:00', 5, 12, 1006, 'Pending'),
    ('2023-05-12', '11:00:00', 6, 13, 1001, 'Completed'),
    ('2023-06-25', '14:00:00', 7, 14, 1002, 'Canceled'),
    ('2023-07-14', '15:00:00', 1, 15, 1003, 'Completed'),

    -- 2024 Appointments
    ('2024-02-15', '09:00:00', 2, 1, 1004, 'Completed'),
    ('2024-03-20', '10:00:00', 3, 2, 1005, 'Pending'),
    ('2024-05-12', '11:00:00', 4, 3, 1006, 'Completed'),
    ('2024-06-25', '14:00:00', 5, 4, 1001, 'Canceled'),
    ('2024-07-14', '15:00:00', 6, 5, 1002, 'Completed'),

    -- 2025 Appointments
    ('2025-02-15', '09:00:00', 2, 1, 1004, 'Completed'),
    ('2025-03-20', '10:00:00', 3, 2, 1005, 'Pending'),
    ('2025-05-12', '11:00:00', 4, 3, 1006, 'Completed'),
    ('2025-06-25', '14:00:00', 5, 4, 1001, 'Canceled'),
    ('2025-07-14', '15:00:00', 6, 5, 1002, 'Completed');



-- Populating BoardingReservations (2021-2024)
INSERT INTO BoardingReservations (StartDate, EndDate, PetID, EmployeeID, Status)
VALUES
    ('2021-02-01', '2021-02-07', 1, 1000, 'Completed'),
    ('2021-04-10', '2021-04-15', 2, 1001, 'Pending'),
    ('2022-02-01', '2022-02-07', 3, 1002, 'Completed'),
    ('2022-04-10', '2022-04-15', 4, 1003, 'Pending'),
    ('2023-02-01', '2023-02-07', 5, 1004, 'Completed'),
    ('2023-04-10', '2023-04-15', 6, 1005, 'Pending'),
    ('2024-02-01', '2024-02-07', 7, 1006, 'Completed'),
    ('2024-04-10', '2024-04-15', 8, 1000, 'Pending'),
    ('2024-05-01', '2024-05-07', 9, 1001, 'Completed'),
    ('2024-06-01', '2024-06-07', 10, 1002, 'Pending'),
    ('2025-02-01', '2025-02-07', 11, 1003, 'Completed'),
    ('2025-04-10', '2025-04-15', 12, 1004, 'Pending'),
    ('2025-05-01', '2025-05-07', 13, 1005, 'Completed'),
    ('2025-06-01', '2025-06-07', 14, 1006, 'Pending'),
    ('2025-07-01', '2025-07-07', 15, 1000, 'Completed');




-- Populating Payments (2021-2024)
INSERT INTO Payments (Amount, PaymentTimestamp, Method, AppointmentID, ReservationID, Status, EmployeeID)
VALUES
    -- 2021 Payments
    (75.00, '2021-04-05 10:30:00', 'Cash', 4, NULL, 'Full Payment', 1000),  -- Appointment for Basic Grooming
    (120.00, '2021-07-14 16:00:00', 'Bank Transfer', NULL, 1, 'Pending', 1001),  -- Reservation for Boarding Service
    (180.00, '2021-08-01 11:00:00', 'Online Payment', 5, NULL, 'Partial Payment', 1002),  -- Appointment for Fur Styling
    (90.00, '2021-10-12 14:30:00', 'Credit Card', NULL, 2, 'Full Payment', 1003),  -- Reservation for Pet Training
    (50.00, '2021-12-05 15:00:00', 'Cash', 6, NULL, 'Refunded', 1004),  -- Appointment for Bathing

    -- 2022 Payments
    (100.00, '2022-01-15 09:00:00', 'Credit Card', 7, NULL, 'Pending', 1005),  -- Appointment for General Checkup
    (300.00, '2022-04-10 13:00:00', 'Cash', NULL, 3, 'Full Payment', 1006),  -- Reservation for Obedience Training
    (200.00, '2022-05-22 10:45:00', 'Bank Transfer', 8, NULL, 'Full Payment', 1000),  -- Appointment for Vaccination
    (250.00, '2022-09-18 11:30:00', 'Online Payment', NULL, 4, 'Voided', 1001),  -- Reservation for Boarding Service
    (300.00, '2022-11-25 14:00:00', 'Debit Card', 9, NULL, 'Partial Payment', 1002),  -- Appointment for Teeth Cleaning

    -- 2023 Payments
    (500.00, '2023-01-05 11:00:00', 'Credit Card', NULL, 5, 'Full Payment', 1003),  -- Reservation for Grooming Package
    (350.00, '2023-02-18 13:15:00', 'Cash', 10, NULL, 'Pending', 1004),  -- Appointment for Fur Styling
    (100.00, '2023-03-03 14:45:00', 'Bank Transfer', 11, NULL, 'Full Payment', 1005),  -- Appointment for Bathing Only
    (150.00, '2023-04-06 10:00:00', 'Cash', NULL, 6, 'Refunded', 1006),  -- Reservation for Training
    (180.00, '2023-05-25 13:00:00', 'Credit Card', NULL, 7, 'Partial Payment', 1000),  -- Reservation for Checkup
    (250.00, '2023-07-12 10:30:00', 'Online Payment', 12, NULL, 'Full Payment', 1001),  -- Appointment for Full Grooming
    (100.00, '2023-09-20 15:30:00', 'Debit Card', 13, NULL, 'Voided', 1002),  -- Appointment for Basic Grooming
    (400.00, '2023-11-02 12:00:00', 'Cash', NULL, 8, 'Full Payment', 1003),  -- Reservation for Obedience Training

    -- 2024 Payments
    (75.00, '2024-01-20 10:00:00', 'Credit Card', NULL, 9, 'Partial Payment', 1004),  -- Reservation for Full Grooming Package
    (100.00, '2024-03-15 14:00:00', 'Bank Transfer', 5, NULL, 'Pending', 1005),  -- Appointment for Teeth Cleaning
    (220.00, '2024-04-05 09:30:00', 'Cash', NULL, 10, 'Full Payment', 1006),  -- Reservation for Boarding
    (300.00, '2024-05-10 13:00:00', 'Credit Card', 15, NULL, 'Partial Payment', 1000),  -- Appointment for Bathing Only
    (120.00, '2024-06-17 11:45:00', 'Debit Card', NULL, 7, 'Refunded', 1001),  -- Reservation for Pet Training
    (250.00, '2024-07-10 15:15:00', 'Online Payment', 16, NULL, 'Full Payment', 1002),  -- Appointment for Grooming Package
    (150.00, '2024-09-05 10:30:00', 'Cash', NULL, 3, 'Full Payment', 1003),  -- Reservation for Obedience Training
    (300.00, '2024-10-11 14:00:00', 'Credit Card', 17, NULL, 'Voided', 1004),  -- Appointment for General Checkup
    (50.00, '2024-11-23 13:00:00', 'Bank Transfer', NULL, 2, 'Pending', 1005);  -- Reservation for Boarding Service

-- Populating Reports
INSERT INTO Reports (ReportTitle, ReportType, ReportTimeStamp, Content, EmployeeID)
VALUES
    ('Monthly Grooming Summary', 'Grooming', '2021-01-31 12:00:00', 'Summary of grooming activities in January', 1000),
    ('Boarding Activity Report', 'Boarding', '2021-02-28 14:00:00', 'Details of boarding activities in February', 1001);
