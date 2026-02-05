GRUPO_Gi3
Nombre de la BD: CRUD_KAP
La URL del repositorio GITHUB: https://github.com/alicia-2004/CRUD_AVANZADO_KAP.git

Cualquier otra información pertinente para ejecutar / desplegar la aplicación.
1. Crear la base de datos en MySQL vacia con el nombre especificado.
2. Configruración de hibernate.cfg.xml:
	- Username de la conexión SQL
	- Contraseña de la conexión SQL
	- Hibernate.hbm2dd1.auto ponerlo en "créate" para que al ejecutar la aplicación por primera vez 	cree las tablas
	- Una vez ejecutada volverlo a cambiar a "update" para que no borre los datos cada vez que 	ejecute la aplicación
	- Hacer las insert
	

Cambios realizados respecto al proyecto de base:
ADT:
	- Añadir las entidades CARD, SHOE, REVIEW y ORDER_ 
	- Crear hibernate.cfg.xml con la conficuración a la base de datos
	- Crear HibernateUtil para abrir la conexión
	- Migrar DBImplements a HibernateImplements
DIN:
	- Añadir la ventana AddProduct
	- Añadir la ventana AdminModifyShoeFXML
	- Añadir la ventana MainPageUser
	- Añadir la ventana PaymentWindowFXML
	- Añadir la ventana ShoeDetail

Especificación de reparto de tareas de tareas correspondiente a ADT y DIN, de cada miembro del grupo.
Kevin:
	ADT:
		- Migrar proyecto de Basic Data Source a Hibernate
		- Metodos: LoadShoes y LoadModels
	DIN:
		- Ventana y controlador:
			- MainPageUser
		-Logger
		- Test:
			- MainPageUser

Alicia:
	ADT:
		- Metodos: generateNewOrderId, dropShoe, updateStockShoe y checkPayments 
	DIN:
		- Ventana y controlador:
			- AdminModifyShoe
			- PaymentWindow
		- Test:
			- AdminModifyShoe
			- PaymentWindow

Pablo:
	ADT:
		- Metodos: addShoe y loadShoeVariants
		- Generar inserts
	DIN:
		- Ventana y controlador:
			- AddProduct
			- ShoeDetail
		- Test:
			- AddProduct
			- ShoeDetail

