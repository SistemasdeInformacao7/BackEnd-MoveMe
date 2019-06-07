DROP DATABASE IF EXISTS moveme;
CREATE DATABASE moveme;
USE moveme;

CREATE TABLE administrador (
id INTEGER PRIMARY KEY AUTO_INCREMENT,
foto VARCHAR(500),
senha VARCHAR(100),
email VARCHAR(100),
nome VARCHAR(100),
cod_recuperacao INT(5)
);

CREATE TABLE usuario (
cpf VARCHAR(11) PRIMARY KEY,
nome VARCHAR(100),
email VARCHAR(100),
senha VARCHAR(100),
telefone VARCHAR(15)
);

CREATE TABLE motorista (
id INTEGER PRIMARY KEY AUTO_INCREMENT,
cnh VARCHAR(30)
nome VARCHAR(100),
email VARCHAR(100),
fone VARCHAR(20),
status TINYINT(1)
);

CREATE TABLE veiculo (
id INTEGER PRIMARY KEY AUTO_INCREMENT,
numero_vagas INTEGER
);

CREATE TABLE restaurante (
id INTEGER PRIMARY KEY AUTO_INCREMENT,
cidade VARCHAR(100),
moeda VARCHAR(100),
cozinha VARCHAR(100),
servico_online INTEGER,
servico_agora INTEGER,
faixa_preco DECIMAL(10)
);

CREATE TABLE viagem (
id INTEGER PRIMARY KEY AUTO_INCREMENT,
dia DATETIME,
nota INTEGER,
cpfusuario VARCHAR(11),
idmotorista INTEGER,
idveiculo INTEGER,
CONSTRAINT FK_motorista FOREIGN KEY(idmotorista) REFERENCES motorista (id),
CONSTRAINT FK_usuario FOREIGN KEY(cpfusuario) REFERENCES usuario (cpf),
CONSTRAINT FK_veiculo FOREIGN KEY(idveiculo) REFERENCES veiculo (id)
);

CREATE TABLE usuario_viagem (
idviagem INTEGER,
cpfusuario VARCHAR(11),
avaliacao INTEGER,
preco DECIMAL,
CONSTRAINT PK_usuario_viagem PRIMARY KEY (idviagem, cpfusuario),
CONSTRAINT FK_viagem FOREIGN KEY (idviagem) REFERENCES viagem(id),
CONSTRAINT FK_usuarioViagem FOREIGN KEY (cpfusuario) REFERENCES usuario(cpf)
);

CREATE TABLE usuario_avalia_restaurante (
id INTEGER PRIMARY KEY AUTO_INCREMENT,
avaliacao INTEGER,
dia DATE,
idrestaurante INTEGER,
cpfusuario VARCHAR(11),
CONSTRAINT FK_restaurante FOREIGN KEY(idrestaurante) REFERENCES restaurante (id),
CONSTRAINT FK_usuario_restaurante FOREIGN KEY(cpfusuario) REFERENCES usuario (cpf)
);

CREATE USER 'master'@'%' IDENTIFIED BY '@Master123';
flush privileges;
grant select,insert,delete,update on moveme.* to 'master'@'%';
flush privileges;








