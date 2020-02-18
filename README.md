# Ejemplo Tarea: Reservas de Aulas
## Profesor: José Ramón Jiménez Reyes

Desde el IES Al-Ándalus nos comentan que debemos tener en cuenta los siguientes aspectos:

- Las aulas se pueden reservar para una permanencia por tramo (de mañana o de tarde) o para una permanencia por horas. La permanencia por horas se hará por horas en punto y no serán anteriores a las 8:00h ni posteriores a las 22:00h.
- Si para un día se realiza una reserva con permanencia por tramo, para ese día no se podrán hacer reservas por horas y viceversa.
- Las aulas deben tener información sobre el número de puestos de cada una.
- Las reservas no se pueden realizar para el mes en curso (sólo para el mes que viene o posteriores).
- Tampoco podemos anular una reserva a no ser que sea para el mes siguiente o posteriores.
- En el centro se lleva un sistema de puntos que cada profesor gasta al hacer una reserva:
    - Una permanencia por tramo restará 10 puntos.
    - Una permanencia por hora restará 3 puntos.
    - Un aula restará 0,5 puntos por el número de puestos del aula.
    - Una reserva restará la suma del número de puntos de la permanencia más el número de puntos del aula.
    - Un profesor tiene disponibles cada mes 200 puntos por lo que si cuando va a realizar una reserva el número de puntos gastados ese mes más el número de puntos de la reserva que quiere realizar supera ese límite no dejará realizar la reserva.

Además queremos aprovechar para implementar el patrón Modelo Vista Controlador en nuestra aplicación.

Por tanto, en este **tercer spring** abarcaremos todos estos requisitos.

Se propone seguir el siguiente diagrama de clases:

![Diagrama de clases para reservasaulas](src/main/resources/org/iesalandalus/programacion/reservasaulas/reservasAulas.png)

Cuenta conmigo para cualquier duda que te pueda surgir o cualquier errata que puedas encontrar.


