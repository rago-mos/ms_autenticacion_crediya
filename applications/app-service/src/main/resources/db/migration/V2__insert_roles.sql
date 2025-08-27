-- Inserta roles si no existen previamente
INSERT INTO rol (nombre, descripcion)
SELECT * FROM (
                  SELECT 'ADMIN' AS nombre, 'Administrador del sistema' AS descripcion
                  UNION ALL
                  SELECT 'ASESOR', 'Usuario con permisos de gestión y asesoría'
                  UNION ALL
                  SELECT 'CLIENTE', 'Usuario final con acceso limitado'
              ) AS nuevos_roles
WHERE NOT EXISTS (
    SELECT 1 FROM rol r WHERE r.nombre = nuevos_roles.nombre
);