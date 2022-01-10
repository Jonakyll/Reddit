INSERT INTO Users(name,password,role) VALUES ('ailton','$2a$10$deFggrPkVf0uyeiB6BxLy.M54uFEOUzsQ9Qp6Jql3l5sujgixaCM.','AUTHENTIFIED')
INSERT INTO Users(name,password,role) VALUES ('fabien','$2a$10$HzCiahsJjqhsq9M1bhrFeel.rW7WzznKY7t9I.6e0I7uarGpOc7hy','AUTHENTIFIED')
INSERT INTO Users(name,password,role) VALUES ('gerald','$2a$10$lYMx0yMQSPtoYOcFolu6TeC7pvHMaVBel1MUSCEKduBtRMdAZ6jzi','AUTHENTIFIED')
INSERT INTO Users(name,password,role) VALUES ('jonathan','$2a$10$1nKRUgICxvLJQ0BCOEeTSuJSGQbgqUqIw8lqE3xg/D778UOOzkqLq','AUTHENTIFIED')
INSERT INTO Users(name,password,role) VALUES ('admin','$2a$10$drKwCelsEMom.wHOrcvRte5haeHFL0XmWffjccwTbQu07yb.igeOy','ADMIN')
INSERT INTO Users(name,password,role) VALUES ('prof','$2a$10$uifyGFTz1H7SMCOBeTWfPu6UpkhvFEQ0Lz.pdaZqUSJe9p8Kq7Z0G','AUTHENTIFIED')

INSERT INTO Topics(date, content, value, title, author_id) VALUES (1613399690019, 'Je ne pense qu''à elle (ma carrière de vtuber), mais je ne suis pas assez chaud en japonais encore :/ Si des gens connaissent des sites pour apprendre le japonais rapidement (full bilingue en à peu près 5min), n''hésitez pas à partager', 0, 'je suis japonais, DOMO', 1)
INSERT INTO Topics(date, content, value, title, author_id) VALUES (1613399690019, 'Bonjour, je m''appelle Fabien et depuis qlq temps, je me fais harceler à la fac par 2 de mes camarades notamment Jonathan et Ailton. J''ai tenté plusieurs fois de les arreter mais il ne comprennent pas que ça m''affecte au plus profond de mon âme..', 0, 'trop, c''est trop.', 2)

--Comment d'un topic
INSERT INTO Comments(content, date, parent_id, value, author_id) VALUES ('Bonjour', 1614936938284, 1, 0, 6)
INSERT INTO topics_answers(topic_id, answers_id) VALUES (1, 1)

--upvote d'un topic
INSERT INTO topics_upvotes(topic_id, upvotes_id) VALUES (1, 6)
UPDATE Topics SET value = 1 WHERE id = 1

--downvote d'un comment
INSERT INTO comments_downvotes(comment_id, downvotes_id) VALUES (1, 4)

--comment d'un comment
INSERT INTO Comments(content, date, parent_id, value, author_id) VALUES ('Coucou', 1614937700764, 1, 0, 4)
INSERT INTO comments_answers(comment_id, answers_id) VALUES (1, 2)