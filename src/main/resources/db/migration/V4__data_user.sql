use course;

INSERT Users (`Id`, `FirstName`, `MiddleName`, `LastName`, `Gender`, `Address`, `NormalizedDisplayName`,
              `PersonalEmail`, `PersonalEmailConfirmed`, `NormalizedPersonalEmail`, `PersonalPhoneNumber`, `CreateBy`,
              `CreateOn`, `UpdateBy`, `UpdateOn`, `UserStatus`, `UserName`, `NormalizedUserName`, `Email`,
              `NormalizedEmail`, `EmailConfirmed`, `PasswordHash`, `SecurityStamp`, `ConcurrencyStamp`, `PhoneNumber`,
              `PhoneNumberConfirmed`, `TwoFactorEnabled`, `LockoutEnd`, `LockoutEnabled`, `AccessFailedCount`,
              `StudentID`, `ProfileImageUrl`, `RoleAccount`)
VALUES (2, 'Bui', 'Ta', 'Hanh', 0, NULL, 'BUI TAN HANH', NULL, 0, NULL, NULL, 'admi',
        CAST('2021-11-23T15:46:59.6397057' AS DATETIME(6)), 'admi', CAST('2021-11-24T12:56:58.1875727' AS DATETIME(6)),
        1, 'admi', 'ADMI', '18120352@student.hcmus.edu.v', '18120352@STUDENT.HCMUS.EDU.V', 1,
        'AQAAAAEAACcQAAAAENc82JjJgW/de72JVujqXzeWsOmqBPuueT3fVwktBWPRV2cQE/DuIRmPfvol4VLNgw==',
        'UYF3HGBN5LULD7MENDLD34X5XGBL3DCP', '020f0f64-d318-486d-a378-a3f828117c37', '0812291357', 0, 0, NULL, 1, 0,
        '18120352', NULL, 0);
INSERT Users (`Id`, `FirstName`, `MiddleName`, `LastName`, `Gender`, `Address`, `NormalizedDisplayName`,
              `PersonalEmail`, `PersonalEmailConfirmed`, `NormalizedPersonalEmail`, `PersonalPhoneNumber`, `CreateBy`,
              `CreateOn`, `UpdateBy`, `UpdateOn`, `UserStatus`, `UserName`, `NormalizedUserName`, `Email`,
              `NormalizedEmail`, `EmailConfirmed`, `PasswordHash`, `SecurityStamp`, `ConcurrencyStamp`, `PhoneNumber`,
              `PhoneNumberConfirmed`, `TwoFactorEnabled`, `LockoutEnd`, `LockoutEnabled`, `AccessFailedCount`,
              `StudentID`, `ProfileImageUrl`, `RoleAccount`)
VALUES (4, 'Dung', NULL, 'Le', 0, NULL, 'DUNG LE', NULL, 0, NULL, NULL, NULL,
        CAST('0001-01-01T00:00:00.0000000' AS DATETIME(6)), 'test', CAST('2021-11-24T21:24:21.7015169' AS DATETIME(6)),
        1, 'test', 'TEST', 'truongsalacuavietnamnhe@gmail.com', 'TRUONGSALACUAVIETNAMNHE@GMAIL.COM', 1,
        'AQAAAAEAACcQAAAAECBi79DEb/5EAL9EYxa47oMEPj+KwX6KYn1yDq8nzi7ytekLGgmMcxIaT4OM+gIkoQ==',
        'GLO2JKTXI43QHU7MACQ2VCMRCOSHH7YH', '2739914a-ab40-4962-ae92-5118ef65ce4b', '090999909', 0, 0, NULL, 1, 0, '2',
        NULL, 0);
INSERT Users (`Id`, `FirstName`, `MiddleName`, `LastName`, `Gender`, `Address`, `NormalizedDisplayName`,
              `PersonalEmail`, `PersonalEmailConfirmed`, `NormalizedPersonalEmail`, `PersonalPhoneNumber`, `CreateBy`,
              `CreateOn`, `UpdateBy`, `UpdateOn`, `UserStatus`, `UserName`, `NormalizedUserName`, `Email`,
              `NormalizedEmail`, `EmailConfirmed`, `PasswordHash`, `SecurityStamp`, `ConcurrencyStamp`, `PhoneNumber`,
              `PhoneNumberConfirmed`, `TwoFactorEnabled`, `LockoutEnd`, `LockoutEnabled`, `AccessFailedCount`,
              `StudentID`, `ProfileImageUrl`, `RoleAccount`)
VALUES (5, 'Phạm', 'Anh', 'Hữu', 0, NULL, 'PHẠM ANH HỮU', 'panhhuu@gmail.com', 0, 'PANHHUU@GMAIL.COM', '0987654321',
        'panhhuu', CAST('2021-11-23T21:22:26.3124170' AS DATETIME(6)), 'panhhuu',
        CAST('2021-11-24T17:06:22.2080856' AS DATETIME(6)), 1, 'panhhuu', 'PANHHUU', 'panhhuu@gmail.com',
        'PANHHUU@GMAIL.COM', 1, 'AQAAAAEAACcQAAAAEGG18TvSv1rjeuvh3gPBPC9urywgaUuJb26xzavKrbHD5T73yrekSQkHUR9RWvWW9A==',
        'PC7XHY4QDKMJWMCOLXK3CKC7BQUFEL3', '6747449c-802a-44e7-b4d9-90d55627c880', '0987654321', 0, 0, null, 1, 3,
        '18120394', NULL, 0);
INSERT Users (`Id`, `FirstName`, `MiddleName`, `LastName`, `Gender`, `Address`, `NormalizedDisplayName`,
              `PersonalEmail`, `PersonalEmailConfirmed`, `NormalizedPersonalEmail`, `PersonalPhoneNumber`, `CreateBy`,
              `CreateOn`, `UpdateBy`, `UpdateOn`, `UserStatus`, `UserName`, `NormalizedUserName`, `Email`,
              `NormalizedEmail`, `EmailConfirmed`, `PasswordHash`, `SecurityStamp`, `ConcurrencyStamp`, `PhoneNumber`,
              `PhoneNumberConfirmed`, `TwoFactorEnabled`, `LockoutEnd`, `LockoutEnabled`, `AccessFailedCount`,
              `StudentID`, `ProfileImageUrl`, `RoleAccount`)
VALUES (6, 'Phạm', 'Anh', 'Hữu', 0, NULL, 'PHẠM ANH HỮU', 'panhhuu@gmail.com', 0, 'PANHHUU@GMAIL.COM', '0987654321',
        'anhhuu', CAST('2021-11-23T22:15:53.9909589' AS DATETIME(6)), 'anhhuu',
        CAST('2021-11-23T22:15:53.9909592' AS DATETIME(6)), 0, 'anhhuu', 'ANHHUU', 'infi9.hcmus@gmail.com',
        'INFI9.HCMUS@GMAIL.COM', 0,
        'AQAAAAEAACcQAAAAEPnCNFacGiFm01ITvB+pyldKy2XWf6Q9scwMmGbngaRD0SIHPICVYBnDbDVoqezs6w==',
        'QFQI4UJKWTTVRYHKYAIGMEG5CRTUUD4X', 'c79e1d96-08ae-4256-9ed7-618649272f35', '0987654321', 0, 0, NULL, 1, 0,
        '18120394', NULL, 0);
INSERT Users (`Id`, `FirstName`, `MiddleName`, `LastName`, `Gender`, `Address`, `NormalizedDisplayName`,
              `PersonalEmail`, `PersonalEmailConfirmed`, `NormalizedPersonalEmail`, `PersonalPhoneNumber`, `CreateBy`,
              `CreateOn`, `UpdateBy`, `UpdateOn`, `UserStatus`, `UserName`, `NormalizedUserName`, `Email`,
              `NormalizedEmail`, `EmailConfirmed`, `PasswordHash`, `SecurityStamp`, `ConcurrencyStamp`, `PhoneNumber`,
              `PhoneNumberConfirmed`, `TwoFactorEnabled`, `LockoutEnd`, `LockoutEnabled`, `AccessFailedCount`,
              `StudentID`, `ProfileImageUrl`, `RoleAccount`)
VALUES (7, 'Hạ', NULL, 'Bùi', 0, NULL, 'HẠ BÙI', NULL, 0, NULL, NULL, NULL,
        CAST('0001-01-01T00:00:00.0000000' AS DATETIME(6)), NULL, CAST('0001-01-01T00:00:00.0000000' AS DATETIME(6)), 1,
        '677SG', '677SG', 'tanhank2k@gmail.com', 'TANHANK2K@GMAIL.COM', 1, NULL, 'CFYSFS3EWCVKRDJ3ZEDQDCBTD3M3H233',
        'd77c2641-8c78-43ce-b550-0af5c991f801', NULL, 0, 0, NULL, 1, 0, '1', NULL, 0);
INSERT Users (`Id`, `FirstName`, `MiddleName`, `LastName`, `Gender`, `Address`, `NormalizedDisplayName`,
              `PersonalEmail`, `PersonalEmailConfirmed`, `NormalizedPersonalEmail`, `PersonalPhoneNumber`, `CreateBy`,
              `CreateOn`, `UpdateBy`, `UpdateOn`, `UserStatus`, `UserName`, `NormalizedUserName`, `Email`,
              `NormalizedEmail`, `EmailConfirmed`, `PasswordHash`, `SecurityStamp`, `ConcurrencyStamp`, `PhoneNumber`,
              `PhoneNumberConfirmed`, `TwoFactorEnabled`, `LockoutEnd`, `LockoutEnabled`, `AccessFailedCount`,
              `StudentID`, `ProfileImageUrl`, `RoleAccount`)
VALUES (8, 'Anh Hữu', NULL, 'Phạm', 0, NULL, 'ANH HỮU PHẠM', NULL, 0, NULL, NULL, NULL,
        CAST('0001-01-01T00:00:00.0000000' AS DATETIME(6)), NULL, CAST('0001-01-01T00:00:00.0000000' AS DATETIME(6)), 1,
        'I59EY', 'I59EY', 'anhhuuqb@gmail.com', 'ANHHUUQB@GMAIL.COM', 1, NULL, 'NVLV4S2QYHXW5JYHL2VCSTDGVSMTLK3D',
        '9a64d15c-c7b1-43e6-a02c-1f463c5021c4', NULL, 0, 0, NULL, 1, 0, NULL, NULL, 0);
INSERT Users (`Id`, `FirstName`, `MiddleName`, `LastName`, `Gender`, `Address`, `NormalizedDisplayName`,
              `PersonalEmail`, `PersonalEmailConfirmed`, `NormalizedPersonalEmail`, `PersonalPhoneNumber`, `CreateBy`,
              `CreateOn`, `UpdateBy`, `UpdateOn`, `UserStatus`, `UserName`, `NormalizedUserName`, `Email`,
              `NormalizedEmail`, `EmailConfirmed`, `PasswordHash`, `SecurityStamp`, `ConcurrencyStamp`, `PhoneNumber`,
              `PhoneNumberConfirmed`, `TwoFactorEnabled`, `LockoutEnd`, `LockoutEnabled`, `AccessFailedCount`,
              `StudentID`, `ProfileImageUrl`, `RoleAccount`)
VALUES (9, 'Pham', NULL, 'Huu', 0, NULL, 'PHAM HUU', NULL, 0, NULL, NULL, NULL,
        CAST('0001-01-01T00:00:00.0000000' AS DATETIME(6)), NULL, CAST('0001-01-01T00:00:00.0000000' AS DATETIME(6)), 1,
        'Y0HBC', 'Y0HBC', 'truongsahoangsalacuavietnamnhe@gmail.com', 'TRUONGSAHOANGSALACUAVIETNAMNHE@GMAIL.COM', 1,
        NULL, 'ZYFRF2IIWFXG3VISXNTIG2ZACSP6AWEY', 'b7d94987-aa7e-45cd-8d0b-df71fc63fa67', NULL, 0, 0, NULL, 1, 0, NULL,
        NULL, 0);
INSERT Users (`Id`, `FirstName`, `MiddleName`, `LastName`, `Gender`, `Address`, `NormalizedDisplayName`,
              `PersonalEmail`, `PersonalEmailConfirmed`, `NormalizedPersonalEmail`, `PersonalPhoneNumber`, `CreateBy`,
              `CreateOn`, `UpdateBy`, `UpdateOn`, `UserStatus`, `UserName`, `NormalizedUserName`, `Email`,
              `NormalizedEmail`, `EmailConfirmed`, `PasswordHash`, `SecurityStamp`, `ConcurrencyStamp`, `PhoneNumber`,
              `PhoneNumberConfirmed`, `TwoFactorEnabled`, `LockoutEnd`, `LockoutEnabled`, `AccessFailedCount`,
              `StudentID`, `ProfileImageUrl`, `RoleAccount`)
VALUES (10, 'Phạm', 'Minh ', 'Anh Hữu', 0, NULL, 'PHẠM MINH  ANH HỮU', NULL, 0, NULL, NULL, 'panhhuu2',
        CAST('2021-11-24T19:06:58.7082862' AS DATETIME(6)), 'panhhuu2',
        CAST('2021-11-24T19:06:58.7082864' AS DATETIME(6)), 1, 'panhhuu2', 'PANHHUU2', 'anhhuudev@gmail.com',
        'ANHHUUDEV@GMAIL.COM', 1,
        'AQAAAAEAACcQAAAAEC1eQQKzphtBc8lVvQhrhsGO/p7mQkHhTdSWfhIXK+vhmAgKAZOOvPhL6ajc+yaozg==',
        'VWM2DABZEF52XZOTNMYTD2MFXMAL7CBF', 'ddd461eb-e711-4a93-be11-27437d18c9fa', '0854503234', 0, 0, NULL, 1, 0,
        NULL, NULL, 0);
INSERT Users (`Id`, `FirstName`, `MiddleName`, `LastName`, `Gender`, `Address`, `NormalizedDisplayName`,
              `PersonalEmail`, `PersonalEmailConfirmed`, `NormalizedPersonalEmail`, `PersonalPhoneNumber`, `CreateBy`,
              `CreateOn`, `UpdateBy`, `UpdateOn`, `UserStatus`, `UserName`, `NormalizedUserName`, `Email`,
              `NormalizedEmail`, `EmailConfirmed`, `PasswordHash`, `SecurityStamp`, `ConcurrencyStamp`, `PhoneNumber`,
              `PhoneNumberConfirmed`, `TwoFactorEnabled`, `LockoutEnd`, `LockoutEnabled`, `AccessFailedCount`,
              `StudentID`, `ProfileImageUrl`, `RoleAccount`)
VALUES (11, 'Phạm', 'Minh ', 'Anh Hữu', 0, NULL, 'PHẠM MINH  ANH HỮU', NULL, 0, NULL, NULL, 'panhhuu3',
        CAST('2021-11-24T19:16:16.5069640' AS DATETIME(6)), 'panhhuu3',
        CAST('2021-11-24T19:16:16.5069642' AS DATETIME(6)), 1, 'panhhuu3', 'PANHHUU3', 'anhhuudev1@gmail.com',
        'ANHHUUDEV1@GMAIL.COM', 1,
        'AQAAAAEAACcQAAAAEB8lwjZ/rq6qMQtIfY7k6ij7QONraoyMVZ4z/sS2v1lsPKjIvgZN2fmHzoCa3fb1KA==',
        'AITJLCFDKTU6DQS4VKRVT76LYAMW2BQZ', 'd3ed6805-6315-43e9-a565-1a71bfe19188', '0854503234', 0, 0, NULL, 1, 0,
        NULL, NULL, 0);
INSERT Users (`Id`, `FirstName`, `MiddleName`, `LastName`, `Gender`, `Address`, `NormalizedDisplayName`,
              `PersonalEmail`, `PersonalEmailConfirmed`, `NormalizedPersonalEmail`, `PersonalPhoneNumber`, `CreateBy`,
              `CreateOn`, `UpdateBy`, `UpdateOn`, `UserStatus`, `UserName`, `NormalizedUserName`, `Email`,
              `NormalizedEmail`, `EmailConfirmed`, `PasswordHash`, `SecurityStamp`, `ConcurrencyStamp`, `PhoneNumber`,
              `PhoneNumberConfirmed`, `TwoFactorEnabled`, `LockoutEnd`, `LockoutEnabled`, `AccessFailedCount`,
              `StudentID`, `ProfileImageUrl`, `RoleAccount`)
VALUES (14, 'le', 'quoc', 'le', 0, NULL, 'LE QUOC LE', NULL, 0, NULL, NULL, 'dungle',
        CAST('2021-11-24T13:51:10.8914779' AS DATETIME(6)), 'dungle',
        CAST('2021-11-24T13:51:10.8914783' AS DATETIME(6)), 1, 'dungle', 'DUNGLE', 'ledungcobra@gmail.com',
        'LEDUNGCOBRA@GMAIL.COM', 1,
        'AQAAAAEAACcQAAAAEAI8hbt0/WtrWxjv+Lwyj2/A+mFptLvmReaaaPnWYwLMUvpMYLkfj9Rb1rqKJnfsqw==',
        '37BB3K3YRHPRSYJYBYRRYJB35FGS5NWY', '3ea3fcca-ac00-4e49-bb41-99bb3d4f4396', '0971663834', 0, 0, NULL, 1, 0,
        NULL, NULL, 0);
INSERT Users (`Id`, `FirstName`, `MiddleName`, `LastName`, `Gender`, `Address`, `NormalizedDisplayName`,
              `PersonalEmail`, `PersonalEmailConfirmed`, `NormalizedPersonalEmail`, `PersonalPhoneNumber`, `CreateBy`,
              `CreateOn`, `UpdateBy`, `UpdateOn`, `UserStatus`, `UserName`, `NormalizedUserName`, `Email`,
              `NormalizedEmail`, `EmailConfirmed`, `PasswordHash`, `SecurityStamp`, `ConcurrencyStamp`, `PhoneNumber`,
              `PhoneNumberConfirmed`, `TwoFactorEnabled`, `LockoutEnd`, `LockoutEnabled`, `AccessFailedCount`,
              `StudentID`, `ProfileImageUrl`, `RoleAccount`)
VALUES (15, 'Bùi', 'Tấ', 'Hạnh', 0, NULL, 'BÙI TẤN HẠNH', 'string', 0, 'STRING', 'string', 'tanhank2k',
        CAST('2021-11-29T18:52:40.3083118' AS DATETIME(6)), 'tanhank2k',
        CAST('2021-11-29T18:52:40.3084089' AS DATETIME(6)), 1, 'tanhank2k', 'TANHANK2K', 'tanhanh2kocean@gmail.com',
        'TANHANH2KOCEAN@GMAIL.COM', 1,
        'AQAAAAEAACcQAAAAEKLG6PIOfH8d+JTviCLy2onUcXGyO/U+GxeayPR5w2c+0wxioOeqrQcQBnFOMvgjqw==',
        'P3FA7ZCYXEKOXLDWU53BADNWL3CK56MS', '7cfee4a1-abf3-4c79-b68f-38da155bb479', '0812291357', 0, 0, NULL, 1, 0, '1',
        NULL, 0);