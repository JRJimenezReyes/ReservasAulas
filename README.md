# Ejemplo Tarea: Reservas de Aulas
## Profesor: José Ramón Jiménez Reyes

Desde el IES Al-Ándalus nos comentan que necesitan que los datos sean persistentes en ficheros.

Por tanto, en este **cuarto spring** abarcaremos la persistencia de datos en ficheros.

- Refactorizaremos para hacer alusión a ficheros en vez de a memoria.
- Serializaremos los objetos de dominio necesarios.
- Añadiremos a las interfaces de las clases de dominio dos métodos: `comenzar` y `terminar` para que cada clase haga lo que deba hacer, si es el caso, al comenzar y al terminar. Para este modelo en concreto, al comenzar se deberán leer los datos del fichero y al terminar se escribirán los datos a fichero.
- Implementaremos dichos métodos en cada una de las clases de negocio para que se comporten como se espera y haremos que sean llamadas desde el modelo. 

Se propone seguir el siguiente diagrama de clases:

![Diagrama de clases para reservasaulas](src/main/resources/org/iesalandalus/programacion/reservasaulas/reservasAulas.png)

Cuenta conmigo para cualquier duda que te pueda surgir o cualquier errata que puedas encontrar.


