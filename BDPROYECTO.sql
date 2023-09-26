create database bdproyecto;
use bdproyecto;

create table ventas
(
id_venta int primary key auto_increment not null,
fecha_venta Date not null,
hora_venta Time not null
);

create table proveedores
(
id_proveedor int primary key auto_increment not null,
nombre_proveedor varchar(30) not null,
telefono_proveedor varchar(10) not null,
correo_proveedor varchar(30) not null,
estado_proveedor bit not null
);

create table marcas
(
id_marcas int primary key auto_increment not null,
nombre_marca varchar(15) not null,
proveedor_id int not null,
descripcion_marca varchar(40) not null,
estado_marca bit not null,
foreign key(proveedor_id) references proveedores(id_proveedor)
);

create table productos
(
id_producto int primary key auto_increment not null,
modelo_producto varchar(40) not null,
tipo_producto varchar(20) not null,
precio_producto float not null,
id_marca_producto int not null,
stock_producto int not null,
imagen_producto longblob,
estado_producto bit not null,
foreign key(id_marca_producto) references marcas(id_marcas)
);

create table registro_ventas
(
id_registro int primary key auto_increment not null,
id_registro_venta int not null,
id_producto_venta int not null,
cantidad_producto int not null,
precio_unitario_producto float not null,
foreign key(id_registro_venta) references ventas(id_venta),
foreign key(id_producto_venta) references productos(id_producto)
);

