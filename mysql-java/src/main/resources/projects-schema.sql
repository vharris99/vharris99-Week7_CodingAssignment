DROP TABLE IF EXISTS project_category;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS step;
DROP TABLE IF EXISTS material;
DROP TABLE IF EXISTS project;

CREATE TABLE project (
		project_id INT AUTO_INCREMENT NOT NULL,
		project_name VARCHAR (128) NOT NULL, 
		estimated_hours DECIMAL (7,2),
		actual_hours DECIMAL (7,2), 
		difficulty INT, 
		notes TEXT,
		PRIMARY KEY (project_id)
);

CREATE TABLE material (
		material_id INT AUTO_INCREMENT NOT NULL,
		project_id INT NOT NULL,
		material_name VARCHAR (128) NOT NULL,
		num_required INT,
		cost DECIMAL (7,2),
		PRIMARY KEY (material_id),
		FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE
);

CREATE TABLE step (
		step_id INT AUTO_INCREMENT NOT NULL,
		project_id INT NOT NULL,
		step_text TEXT NOT NULL,
		step_order INT NOT NULL,
		PRIMARY KEY (step_id),
		FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE
);

CREATE TABLE category (
		category_id INT AUTO_INCREMENT NOT NULL,
		category_name VARCHAR (128) NOT NULL,
		PRIMARY KEY (category_id)
);

CREATE TABLE project_category (
		project_id INT NOT NULL,
		category_id INT NOT NULL,
		FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE,
		FOREIGN KEY (category_id) REFERENCES category (category_id) ON DELETE CASCADE,
		UNIQUE KEY	(project_id, category_id)
);

use projects;

INSERT INTO project (project_name, estimated_hours, actual_hours, difficulty, notes)
VALUES ('Change a lightbulb', 1, 1, 1, 'Careful! Bulb may be hot'),
	   ('Mow the lawn', 4, 5, 4, 'Very hot today. Bring extra water'),
	   ('Fix the roof', 4, 6, 5, 'Leaky roof no more!');

INSERT INTO category (category_name)
VALUES ('Lights and Appliances'),
	   ('Gardening'),
	   ('Home Maintenance');
	  
SELECT * FROM project;

INSERT INTO material (project_id, material_name, num_required)
VALUES (1, '28W light bulb', 6),
	   (2, 'Lawnmower', 1),
	   (3, 'Roof shingles', 20);

INSERT INTO step (project_id, step_text, step_order)
VALUES (1, 'Make sure light switch is turned to OFF prior to changing bulb', 1),
	   (1, 'Unscrew the old bulb', 2),
	   (1, 'Screw in the new bulb', 3),
	   (2, 'Pickup any sticks or rocks before mowing', 1),
	   (3, 'Find a latter and locate the hole in the roof', 1);

INSERT INTO project_category (project_id, category_id)
VALUES (1, 1);