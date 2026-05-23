-- Insertamos un par de reservas de ejemplo en el esquema bd_reservas
INSERT INTO bd_reservas.reservas (habitacion_id, huesped_id, fecha_entrada, fecha_salida, monto_total, estado)
VALUES 
(1, 1, '2026-06-01', '2026-06-05', 320000.0, 'CONFIRMADA'),
(2, 2, '2026-07-10', '2026-07-12', 150000.0, 'CONFIRMADA');