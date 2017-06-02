USE [baya]
GO
SET IDENTITY_INSERT [dbo].[Zaposleni] ON 

INSERT [dbo].[Zaposleni] ([id], [KORISNICKO_IME], [LOZINKA]) VALUES (1, N'nata', N'$2a$12$pekU.5U1RJPvxMJSdJn0ZOANJDiTKCyHZtmNDnHaHzhiUsAdyT7Iu')
INSERT [dbo].[Zaposleni] ([id], [KORISNICKO_IME], [LOZINKA]) VALUES (2, N'keke', N'$2a$12$ntRxB2CXAQl8w/z7KAVnVuzuwJiBWdJ/aRgjkQreq0mnNAadWs4Vm')
INSERT [dbo].[Zaposleni] ([id], [KORISNICKO_IME], [LOZINKA]) VALUES (23, N'nene', N'$2a$12$TvdYyzZvlFQO5sDJuzZPHeURvYaCgYRd2FG1ybedpFMFW8AfhuqVe')
INSERT [dbo].[Zaposleni] ([id], [KORISNICKO_IME], [LOZINKA]) VALUES (24, N'dada', N'$2a$12$DjjBkMwCNMre8uBwhLFUKOl5sLHAO908hzLPFTJY7bURyYoGhiEca')
SET IDENTITY_INSERT [dbo].[Zaposleni] OFF
SET IDENTITY_INSERT [dbo].[Uloga] ON 

INSERT [dbo].[Uloga] ([id], [NAZIV_ULOGE]) VALUES (1, N'admin')
INSERT [dbo].[Uloga] ([id], [NAZIV_ULOGE]) VALUES (2, N'salterusa')
SET IDENTITY_INSERT [dbo].[Uloga] OFF
SET IDENTITY_INSERT [dbo].[UlogaZaposlenog] ON 

INSERT [dbo].[UlogaZaposlenog] ([id], [uloga_id], [zaposleni_id]) VALUES (1, 1, 1)
INSERT [dbo].[UlogaZaposlenog] ([id], [uloga_id], [zaposleni_id]) VALUES (2, 2, 2)
SET IDENTITY_INSERT [dbo].[UlogaZaposlenog] OFF
SET IDENTITY_INSERT [dbo].[Permisija] ON 

INSERT [dbo].[Permisija] ([id], [NAZIV_PERMISIJE]) VALUES (1, N'zaposleni.view')
INSERT [dbo].[Permisija] ([id], [NAZIV_PERMISIJE]) VALUES (2, N'zaposleni.create')
INSERT [dbo].[Permisija] ([id], [NAZIV_PERMISIJE]) VALUES (3, N'zaposleni.remove')
SET IDENTITY_INSERT [dbo].[Permisija] OFF
SET IDENTITY_INSERT [dbo].[PermisijaUloge] ON 

INSERT [dbo].[PermisijaUloge] ([id], [permisija_id], [uloga_id]) VALUES (1, 1, 1)
INSERT [dbo].[PermisijaUloge] ([id], [permisija_id], [uloga_id]) VALUES (2, 2, 1)
INSERT [dbo].[PermisijaUloge] ([id], [permisija_id], [uloga_id]) VALUES (3, 3, 1)
SET IDENTITY_INSERT [dbo].[PermisijaUloge] OFF
