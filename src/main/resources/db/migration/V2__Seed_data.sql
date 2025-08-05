INSERT INTO Donor (email, full_name) VALUES
                                         ('admin@hei.school', 'Administrateur Tsinjo'),
                                         ('contact@tsinjo.org', 'Équipe Tsinjo');

INSERT INTO Beneficiary (email, full_name) VALUES
                                               ('urgence@hei.school', 'Service Urgence HEI'),
                                               ('solidarite@tsinjo.org', 'Commission Solidarité');

INSERT INTO Payment (id, psp_type, amount, status) VALUES
                                                       ('INIT_SYS_001', 'ORANGE_MONEY', 10000, 'SUCCEEDED'),
                                                       ('INIT_SYS_002', 'MVOLA', 20000, 'SUCCEEDED');

COMMENT ON TABLE Payment IS 'Stocke tous les paiements via API Vola';
COMMENT ON COLUMN Payment.amount IS 'Montant en unités (1 AR = 1 unité)';