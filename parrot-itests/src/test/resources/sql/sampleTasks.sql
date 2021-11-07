insert into "assignee" (id, name) values ('d866978f-88fa-475f-82cb-a2690dc8b22e'::UUID, 'kesha'),
                                         ('e8d546ab-caca-431c-8b42-89930e4081da'::UUID, 'gosha'),
                                         ('01b817ac-1656-47e1-b3ee-5702fc247c19'::UUID, 'ara');


insert into "task" (description, assignee, status) values ('do that', 'd866978f-88fa-475f-82cb-a2690dc8b22e'::UUID, 'IN_PROGRESS'),
                                                          ('do this', 'e8d546ab-caca-431c-8b42-89930e4081da'::UUID, 'IN_PROGRESS'),
                                                          ('do smth', '01b817ac-1656-47e1-b3ee-5702fc247c19'::UUID, 'DONE');
