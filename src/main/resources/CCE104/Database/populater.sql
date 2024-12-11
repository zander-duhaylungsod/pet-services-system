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

INSERT INTO Pets (Name, Species, Breed, PetImagePath, PetNotes, Age, OwnerID)
VALUES
    ('Buddy', 'Dog', 'Aspin', 'images/buddy.jpg', 'Very friendly and loves playing fetch.', 3, 1),
    ('Mingming', 'Cat', 'Puspin', 'images/mingming.jpg', 'Prefers quiet environments and loves napping.', 2, 2),
    ('Pipoy', 'Bird', 'Lovebird', 'images/pipoy.jpg', 'Enjoys chirping and being near windows.', 1, 3),
    ('Chico', 'Dog', 'Shih Tzu', 'images/chico.jpg', 'Requires special shampoo for sensitive skin.', 4, 4),
    ('Max', 'Dog', 'Golden Retriever', 'images/max.jpg', 'Highly energetic and great with kids.', 5, 1),
    ('Bella', 'Cat', 'Persian', 'images/bella.jpg', 'Needs regular grooming to maintain fur.', 3, 3),
    ('Tweety', 'Bird', 'Canary', 'images/tweety.jpg', 'Loves singing in the mornings.', 2, 2),
    ('Rocky', 'Dog', 'Bulldog', 'images/rocky.jpg', 'Prone to overheating; needs frequent hydration.', 6, 4);

INSERT INTO Employees (FirstName, LastName, Phone, Role)
VALUES
    ('Zander', 'Duhaylungsod', '09854263832', 'Admin'),
    ('Nazlah', 'Nanding', '09547812564', 'Admin'),
    ('John Erold', 'Miano', '09872154654', 'Admin'),
    ('Jaz Cyrill', 'Samano', '09893154742', 'Admin'),
    ('Sarah', 'Carpenter', '09171234567', 'Manager'),
    ('Maria', 'Santos', '09172345678', 'Receptionist'),
    ('Pedro', 'Gomez', '09173456789', 'Groomer'),
    ('Liza', 'Reyes', '09174567890', 'Boarding Attendant'),
    ('Roberto', 'Martinez', '09175678901', 'Veterinarian'),
    ('Carmen', 'Dizon', '09176789012', 'Pet Trainer'),
    ('Antonio', 'Garcia', '09177890123', 'Cleaning Staff');

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
    ('Full Grooming Package', 1000.00, 'Complete grooming package including bathing, nail trimming, and fur styling.'),
    ('Bathing Only', 300.00, 'Basic cleaning and drying service for pets.'),
    ('Fur Styling', 500.00, 'Custom haircuts and trims based on pet breed and owner preferences.'),
    ('Teeth Cleaning', 400.00, 'Dental hygiene service for pets, ensuring clean teeth and fresh breath.'),
    ('Basic Obedience Training', 1500.00, 'Introductory training to teach pets basic commands like sit, stay, and heel.'),
    ('General Checkup', 800.00, 'Health assessment to ensure the pet is in good condition.'),
    ('Full Package Vaccination', 2000.00, 'Comprehensive vaccination package covering essential pet immunizations.');
--Roles: 'Manager', 'Receptionist', 'Groomer', 'Boarding Attendant'

INSERT INTO Appointments (Date, Time, ServiceID, PetID, EmployeeID, Status)
VALUES
    ('2024-12-05', '09:00:00', 1, 1,  1, 'Pending'),
    ('2024-12-06', '10:00:00', 2, 2,  1, 'Confirmed'),
    ('2024-12-07', '11:00:00', 3, 3, 2, 'Cancelled'),
    ('2024-12-08', '14:00:00', 4, 4, 3, 'Pending');

INSERT INTO Payments (Amount, PaymentDate, Method, AppointmentID, Status)
VALUES
    (500.00, '2024-12-05', 'Cash', 1, 'Paid'),
    (800.00, '2024-12-06', 'Credit Card', 2, 'Paid'),
    (0.00, '2024-12-07', 'None', 3, 'Unpaid'),
    (1200.00, '2024-12-08', 'GCash', 4, 'Paid');

INSERT INTO BoardingReservations (StartDate, EndDate, PetID, EmployeeID, Status)
VALUES
    ('2024-12-10', '2024-12-12', 1, 2, 'Pending'),
    ('2024-12-13', '2024-12-15', 2, 3, 'Confirmed'),
    ('2024-12-16', '2024-12-18', 3, 4, 'Cancelled'),
    ('2024-12-19', '2024-12-20', 4, 1, 'Pending');

INSERT INTO Reports (ReportTitle, ReportType, ReportDate, Content, EmployeeID)
VALUES
    ('Monthly Sales Report', 'Sales', '2024-12-01 10:00:00', 'This report covers the sales for November 2024.', 1),
    ('Employee Performance Review', 'HR', '2024-12-01 11:00:00', 'Performance evaluation for all staff.', 2),
    ('Pet Health Report', 'Pet Care', '2024-12-01 12:00:00', 'Health details of pets under care.', 3);

INSERT INTO Refunds (PaymentID, RefundDate, RefundAmount, Reason)
VALUES
    (3, '2024-12-07', 300.00, 'Service cancelled by customer'),
    (4, '2024-12-08', 200.00, 'Service incomplete'),
    (2, '2024-12-09', 100.00, 'Overpayment adjustment');
