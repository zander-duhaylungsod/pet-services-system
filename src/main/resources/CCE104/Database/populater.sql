INSERT INTO Owners (FirstName, LastName, Email, Phone)
VALUES
    ('John', 'Doe', 'john.doe@example.com', '1234567890'),
    ('Jane', 'Smith', 'jane.smith@example.com', '0987654321'),
    ('Robert', 'Brown', 'robert.brown@example.com', '5678901234'),
    ('Emily', 'Davis', 'emily.davis@example.com', '3456789012'),
    ('Michael', 'Johnson', 'michael.johnson@example.com', '2345678901'),
    ('Laura', 'Wilson', 'laura.wilson@example.com', '4567890123'),
    ('David', 'Martinez', 'david.martinez@example.com', '6789012345'),
    ('Emma', 'Taylor', 'emma.taylor@example.com', '7890123456'),
    ('Daniel', 'Garcia', 'daniel.garcia@example.com', '8901234567'),
    ('Sophia', 'Anderson', 'sophia.anderson@example.com', '9012345678');

INSERT INTO Pets (Name, Species, Breed, Age, OwnerID, PetImagePath)
VALUES
    ('Buddy', 'Dog', 'Golden Retriever', 3, 1, '/images/pets/buddy.jpg'),
    ('Max', 'Dog', 'German Shepherd', 5, 2, '/images/pets/max.jpg'),
    ('Mittens', 'Cat', 'Siamese', 2, 3, '/images/pets/mittens.jpg'),
    ('Charlie', 'Dog', 'Bulldog', 4, 4, '/images/pets/charlie.jpg'),
    ('Bella', 'Cat', 'Persian', 3, 5, '/images/pets/bella.jpg'),
    ('Rocky', 'Dog', 'Labrador', 6, 6, '/images/pets/rocky.jpg'),
    ('Daisy', 'Rabbit', 'Holland Lop', 1, 7, '/images/pets/daisy.jpg'),
    ('Luna', 'Cat', 'Maine Coon', 4, 8, '/images/pets/luna.jpg'),
    ('Jack', 'Dog', 'Beagle', 2, 9, '/images/pets/jack.jpg'),
    ('Coco', 'Bird', 'Parakeet', 1, 10, '/images/pets/coco.jpg');

INSERT INTO Branch (BranchName, Location, Phone)
VALUES
    ('Matina Branch', 'Matina Crossing, Davao City', '09853675743'),
    ('Bajada Branch', 'Bajada, Davao City', '09351234567'),
    ('Toril Branch', 'Toril, Davao City', '09987654321');

INSERT INTO Employees (FirstName, LastName, Phone, Role, BranchID)
VALUES
    ( 'Mellon', 'Manifester', '09854363513', 'Administrator', 1),
    ( 'Angel', 'Semora',  '09346545685', 'Manager', 1), -- Will be set as Manager
    ('Juno', 'Carpenter',  '09124567890', 'Receptionist', 2),
    ( 'Kyla', 'Ramos', '09233456789', 'Veterinarian', 3);

UPDATE Branch
SET ManagerID = (SELECT EmployeeID FROM Employees WHERE FirstName = 'Angel' AND Role = 'Manager')
WHERE BranchName = 'Matina Branch';

INSERT INTO Owners (FirstName, LastName, Email, Phone)
VALUES
    ('Maria', 'Santos', 'maria.santos@example.com', '09123456789'),
    ('Juan', 'Cruz', 'juan.cruz@example.com', '09234567890'),
    ('Elena', 'Garcia', NULL, '09345678901'),
    ('Pedro', 'Lopez', 'pedro.lopez@example.com', '09456789012');

INSERT INTO Pets (Name, Species, Breed, Age, OwnerID, PetImagePath)
VALUES
    ('Buddy', 'Dog', 'Aspin', 3, 1, 'images/buddy.jpg'),
    ('Mingming', 'Cat', 'Puspin', 2, 2, 'images/mingming.jpg'),
    ('Pipoy', 'Bird', 'Lovebird', 1, 3, 'images/pipoy.jpg'),
    ('Chico', 'Dog', 'Shih Tzu', 4, 4, 'images/chico.jpg');

INSERT INTO Services (ServiceName, Price, Description)
VALUES
    ('Vaccination', 500.00, 'Complete vaccination for pets'),
    ('Grooming', 800.00, 'Full grooming services for dogs and cats'),
    ('Check-Up', 300.00, 'General pet health check-up'),
    ('Boarding', 1200.00, 'Overnight pet boarding service');

INSERT INTO Appointments (Date, Time, ServiceID, PetID, EmployeeID, BranchID, Status)
VALUES
    ('2024-12-05', '09:00:00', 1, 1, 4, 1, 'Pending'),
    ('2024-12-06', '10:00:00', 2, 2, 4, 1, 'Confirmed'),
    ('2024-12-07', '11:00:00', 3, 3, 3, 2, 'Cancelled'),
    ('2024-12-08', '14:00:00', 4, 4, 2, 3, 'Pending');

INSERT INTO Payments (Amount, PaymentDate, Method, AppointmentID, Status)
VALUES
    (500.00, '2024-12-05', 'Cash', 1, 'Paid'),
    (800.00, '2024-12-06', 'Credit Card', 2, 'Paid'),
    (0.00, '2024-12-07', 'None', 3, 'Unpaid'),
    (1200.00, '2024-12-08', 'GCash', 4, 'Paid');

INSERT INTO BoardingReservations (StartDate, EndDate, PetID, BranchID, EmployeeID, Status)
VALUES
    ('2024-12-10', '2024-12-12', 1, 1, 2, 'Pending'),
    ('2024-12-13', '2024-12-15', 2, 2, 3, 'Confirmed'),
    ('2024-12-16', '2024-12-18', 3, 3, 4, 'Cancelled'),
    ('2024-12-19', '2024-12-20', 4, 1, 1, 'Pending');

INSERT INTO Reports (ReportTitle, ReportType, GeneratedDate, Content, CreatedBy)
VALUES
    ('Monthly Sales Report', 'Sales', '2024-12-01 10:00:00', 'This report covers the sales for November 2024.', 1),
    ('Employee Performance Review', 'HR', '2024-12-01 11:00:00', 'Performance evaluation for all staff.', 2),
    ('Pet Health Report', 'Pet Care', '2024-12-01 12:00:00', 'Health details of pets under care.', 3);

INSERT INTO Refunds (PaymentID, RefundDate, RefundAmount, Reason)
VALUES
    (3, '2024-12-07', 300.00, 'Service cancelled by customer'),
    (4, '2024-12-08', 200.00, 'Service incomplete'),
    (2, '2024-12-09', 100.00, 'Overpayment adjustment');
