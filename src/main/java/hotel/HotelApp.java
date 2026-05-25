package hotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class HotelApp extends JFrame {

    private final GestionHotel hotel = new GestionHotel();
    private final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // colores
    private static final Color C_BG      = new Color(245, 247, 250);
    private static final Color C_PRIMARY = new Color(30, 90, 160);
    private static final Color C_ACCENT  = new Color(0, 153, 102);
    private static final Color C_DANGER  = new Color(192, 0, 0);
    private static final Color C_WHITE   = Color.WHITE;
    private static final Font  F_TITLE   = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font  F_BODY    = new Font("Segoe UI", Font.PLAIN, 12);

    public HotelApp() {
        setTitle("Sistema de Gestión de Hotel — MVP");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 680);
        setLocationRelativeTo(null);
        setBackground(C_BG);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(F_TITLE);
        tabs.setBackground(C_BG);

        tabs.addTab("🏠 Habitaciones",  buildHabitacionesPanel());
        tabs.addTab("👤 Clientes",       buildClientesPanel());
        tabs.addTab("📋 Reservas",       buildReservasPanel());
        tabs.addTab("✅ Check-in/out",   buildCheckPanel());
        tabs.addTab("🧾 Facturas",       buildFacturasPanel());

        add(tabs);
        cargarDatosDemo();
        setVisible(true);
    }

    // ── utilidades ──────────────────────────────────────────────────────────

    private JPanel card(String titulo) {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(C_BG);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        if (!titulo.isEmpty()) {
            JLabel lbl = new JLabel(titulo);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lbl.setForeground(C_PRIMARY);
            lbl.setBorder(new EmptyBorder(0, 0, 8, 0));
            p.add(lbl, BorderLayout.NORTH);
        }
        return p;
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(F_TITLE);
        b.setBackground(bg);
        b.setForeground(C_WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(160, 34));
        return b;
    }

    private JTextField field(int cols) {
        JTextField f = new JTextField(cols);
        f.setFont(F_BODY);
        return f;
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_BODY);
        return l;
    }

    private JTable makeTable(String[] cols) {
        DefaultTableModel m = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable t = new JTable(m);
        t.setFont(F_BODY);
        t.setRowHeight(24);
        t.getTableHeader().setFont(F_TITLE);
        t.getTableHeader().setBackground(C_PRIMARY);
        t.getTableHeader().setForeground(C_WHITE);
        t.setSelectionBackground(new Color(210, 228, 255));
        t.setGridColor(new Color(220, 220, 220));
        return t;
    }

    private DefaultTableModel model(JTable t) {
        return (DefaultTableModel) t.getModel();
    }

    private void ok(String msg)  { JOptionPane.showMessageDialog(this, msg, "OK",    JOptionPane.INFORMATION_MESSAGE); }
    private void err(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }

    // ── HABITACIONES ─────────────────────────────────────────────────────────

    private JPanel buildHabitacionesPanel() {
        JPanel root = card("Gestión de Habitaciones");

        // formulario
        JTextField fNum    = field(5);
        JComboBox<TipoHabitacion> fTipo = new JComboBox<>(TipoHabitacion.values());
        fTipo.setFont(F_BODY);
        JTextField fPrecio = field(8);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        form.setBackground(C_BG);
        form.add(lbl("Número:")); form.add(fNum);
        form.add(lbl("Tipo:"));   form.add(fTipo);
        form.add(lbl("Precio/noche S/.:")); form.add(fPrecio);

        // tabla
        String[] cols = {"Número", "Tipo", "Precio/noche", "Estado"};
        JTable tabla = makeTable(cols);

        JButton btnAgregar = btn("Agregar", C_PRIMARY);
        JButton btnRefresh = btn("Actualizar", C_ACCENT);

        btnAgregar.addActionListener(e -> {
            try {
                int num = Integer.parseInt(fNum.getText().trim());
                double precio = Double.parseDouble(fPrecio.getText().trim());
                TipoHabitacion tipo = (TipoHabitacion) fTipo.getSelectedItem();
                hotel.registrarHabitacion(num, tipo, precio);
                refrescarHabitaciones(tabla);
                fNum.setText(""); fPrecio.setText("");
                ok("Habitación " + num + " registrada.");
            } catch (NumberFormatException ex) {
                err("Número y precio deben ser numéricos.");
            } catch (Exception ex) {
                err(ex.getMessage());
            }
        });

        btnRefresh.addActionListener(e -> refrescarHabitaciones(tabla));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        botones.setBackground(C_BG);
        botones.add(btnAgregar); botones.add(btnRefresh);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(C_BG);
        top.add(form, BorderLayout.CENTER);
        top.add(botones, BorderLayout.SOUTH);

        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return root;
    }

    private void refrescarHabitaciones(JTable t) {
        model(t).setRowCount(0);
        for (Habitacion h : hotel.listarHabitaciones()) {
            model(t).addRow(new Object[]{
                h.getNumero(), h.getTipo(),
                String.format("S/. %.2f", h.getPrecioPorNoche()),
                h.getEstado()
            });
        }
    }

    // ── CLIENTES ─────────────────────────────────────────────────────────────

    private JPanel buildClientesPanel() {
        JPanel root = card("Registro de Clientes");

        JTextField fDni   = field(10);
        JTextField fNom   = field(12);
        JTextField fApe   = field(12);
        JTextField fTel   = field(10);
        JTextField fEmail = field(16);

        JPanel form = new JPanel(new GridLayout(2, 6, 8, 6));
        form.setBackground(C_BG);
        form.add(lbl("DNI:")); form.add(fDni);
        form.add(lbl("Nombre:")); form.add(fNom);
        form.add(lbl("Apellido:")); form.add(fApe);
        form.add(lbl("Teléfono:")); form.add(fTel);
        form.add(lbl("Email:")); form.add(fEmail);
        form.add(new JLabel());

        String[] cols = {"DNI", "Nombre", "Apellido", "Teléfono", "Email"};
        JTable tabla = makeTable(cols);

        JButton btnReg     = btn("Registrar", C_PRIMARY);
        JButton btnRefresh = btn("Actualizar", C_ACCENT);

        btnReg.addActionListener(e -> {
            try {
                hotel.registrarCliente(
                    fDni.getText().trim(), fNom.getText().trim(),
                    fApe.getText().trim(), fTel.getText().trim(),
                    fEmail.getText().trim()
                );
                refrescarClientes(tabla);
                fDni.setText(""); fNom.setText(""); fApe.setText("");
                fTel.setText(""); fEmail.setText("");
                ok("Cliente registrado correctamente.");
            } catch (Exception ex) { err(ex.getMessage()); }
        });

        btnRefresh.addActionListener(e -> refrescarClientes(tabla));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        botones.setBackground(C_BG);
        botones.add(btnReg); botones.add(btnRefresh);

        JPanel top = new JPanel(new BorderLayout(0, 6));
        top.setBackground(C_BG);
        top.add(form, BorderLayout.CENTER);
        top.add(botones, BorderLayout.SOUTH);

        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return root;
    }

    private void refrescarClientes(JTable t) {
        model(t).setRowCount(0);
        for (Cliente c : hotel.listarClientes()) {
            model(t).addRow(new Object[]{
                c.getDni(), c.getNombre(), c.getApellido(),
                c.getTelefono(), c.getEmail()
            });
        }
    }

    // ── RESERVAS ─────────────────────────────────────────────────────────────

    private JPanel buildReservasPanel() {
        JPanel root = card("Gestión de Reservas");

        JTextField fDni    = field(10);
        JTextField fHab    = field(5);
        JTextField fEntrada = field(10);
        JTextField fSalida  = field(10);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        form.setBackground(C_BG);
        form.add(lbl("DNI Cliente:")); form.add(fDni);
        form.add(lbl("N° Habitación:")); form.add(fHab);
        form.add(lbl("Entrada (dd/MM/yyyy):")); form.add(fEntrada);
        form.add(lbl("Salida (dd/MM/yyyy):")); form.add(fSalida);

        String[] cols = {"ID Reserva", "Cliente", "Habitación", "Entrada", "Salida", "Noches", "Costo", "Estado"};
        JTable tabla = makeTable(cols);

        JButton btnCrear   = btn("Crear Reserva", C_PRIMARY);
        JButton btnCancelar = btn("Cancelar", C_DANGER);
        JButton btnRefresh = btn("Actualizar", C_ACCENT);

        btnCrear.addActionListener(e -> {
            try {
                LocalDate entrada = LocalDate.parse(fEntrada.getText().trim(), FMT);
                LocalDate salida  = LocalDate.parse(fSalida.getText().trim(), FMT);
                int numHab = Integer.parseInt(fHab.getText().trim());
                Reserva r = hotel.crearReserva(fDni.getText().trim(), numHab, entrada, salida);
                refrescarReservas(tabla);
                fDni.setText(""); fHab.setText(""); fEntrada.setText(""); fSalida.setText("");
                ok("Reserva creada: " + r.getId());
            } catch (DateTimeParseException ex) {
                err("Formato de fecha incorrecto. Use dd/MM/yyyy");
            } catch (Exception ex) { err(ex.getMessage()); }
        });

        btnCancelar.addActionListener(e -> {
            int row = tabla.getSelectedRow();
            if (row < 0) { err("Seleccione una reserva de la tabla."); return; }
            String id = (String) model(tabla).getValueAt(row, 0);
            try {
                hotel.cancelarReserva(id);
                refrescarReservas(tabla);
                ok("Reserva " + id + " cancelada.");
            } catch (Exception ex) { err(ex.getMessage()); }
        });

        btnRefresh.addActionListener(e -> refrescarReservas(tabla));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        botones.setBackground(C_BG);
        botones.add(btnCrear); botones.add(btnCancelar); botones.add(btnRefresh);

        JPanel top = new JPanel(new BorderLayout(0, 6));
        top.setBackground(C_BG);
        top.add(form, BorderLayout.CENTER);
        top.add(botones, BorderLayout.SOUTH);

        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return root;
    }

    private void refrescarReservas(JTable t) {
        model(t).setRowCount(0);
        for (Reserva r : hotel.listarReservas()) {
            model(t).addRow(new Object[]{
                r.getId(),
                r.getCliente().getNombreCompleto(),
                r.getHabitacion().getNumero(),
                r.getFechaEntrada().format(FMT),
                r.getFechaSalida().format(FMT),
                r.getNochesReservadas(),
                String.format("S/. %.2f", r.calcularCostoTotal()),
                r.getEstado()
            });
        }
    }

    // ── CHECK-IN / CHECK-OUT ─────────────────────────────────────────────────

    private JPanel buildCheckPanel() {
        JPanel root = card("Check-in / Check-out");

        JTextField fId = field(12);
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        form.setBackground(C_BG);
        form.add(lbl("ID Reserva:")); form.add(fId);

        JButton btnCI = btn("Check-in  ✅", C_ACCENT);
        JButton btnCO = btn("Check-out 🧾", C_PRIMARY);

        JTextArea log = new JTextArea(16, 60);
        log.setFont(new Font("Courier New", Font.PLAIN, 12));
        log.setEditable(false);
        log.setBackground(new Color(248, 248, 248));

        btnCI.addActionListener(e -> {
            try {
                String id = fId.getText().trim();
                hotel.realizarCheckIn(id);
                log.append("[CHECK-IN]  Reserva " + id + " — estado: ACTIVA\n");
                fId.setText("");
            } catch (Exception ex) { err(ex.getMessage()); }
        });

        btnCO.addActionListener(e -> {
            try {
                String id = fId.getText().trim();
                Factura f = hotel.realizarCheckOut(id);
                log.append("─────────────────────────────────────────────\n");
                log.append("[CHECK-OUT] Reserva " + id + " — estado: FINALIZADA\n");
                log.append("  Factura N°  : " + f.getNumeroFactura() + "\n");
                log.append("  Cliente     : " + f.getReserva().getCliente().getNombreCompleto() + "\n");
                log.append("  Habitación  : " + f.getReserva().getHabitacion().getNumero() + "\n");
                log.append("  Noches      : " + f.getReserva().getNochesReservadas() + "\n");
                log.append("  Subtotal    : S/. " + String.format("%.2f", f.getSubtotal()) + "\n");
                log.append("  IGV (18%)   : S/. " + String.format("%.2f", f.getIgv()) + "\n");
                log.append("  TOTAL       : S/. " + String.format("%.2f", f.getTotal()) + "\n");
                log.append("─────────────────────────────────────────────\n");
                fId.setText("");
            } catch (Exception ex) { err(ex.getMessage()); }
        });

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        botones.setBackground(C_BG);
        botones.add(btnCI); botones.add(btnCO);

        JPanel top = new JPanel(new BorderLayout(0, 6));
        top.setBackground(C_BG);
        top.add(form, BorderLayout.CENTER);
        top.add(botones, BorderLayout.SOUTH);

        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(log), BorderLayout.CENTER);
        return root;
    }

    // ── FACTURAS ─────────────────────────────────────────────────────────────

    private JPanel buildFacturasPanel() {
        JPanel root = card("Historial de Facturas");

        String[] cols = {"N° Factura", "Cliente", "Reserva", "Noches", "Subtotal", "IGV", "Total", "Emisión"};
        JTable tabla = makeTable(cols);

        JButton btnRefresh = btn("Actualizar", C_ACCENT);
        btnRefresh.addActionListener(e -> refrescarFacturas(tabla));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(C_BG);
        top.add(btnRefresh);

        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return root;
    }

    private void refrescarFacturas(JTable t) {
        model(t).setRowCount(0);
        for (Factura f : hotel.listarFacturas()) {
            model(t).addRow(new Object[]{
                f.getNumeroFactura(),
                f.getReserva().getCliente().getNombreCompleto(),
                f.getReserva().getId(),
                f.getReserva().getNochesReservadas(),
                String.format("S/. %.2f", f.getSubtotal()),
                String.format("S/. %.2f", f.getIgv()),
                String.format("S/. %.2f", f.getTotal()),
                f.getFechaEmision().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            });
        }
    }

    // ── DATOS DEMO ────────────────────────────────────────────────────────────

    private void cargarDatosDemo() {
        try {
            hotel.registrarHabitacion(101, TipoHabitacion.SIMPLE, 120.0);
            hotel.registrarHabitacion(102, TipoHabitacion.DOBLE,  200.0);
            hotel.registrarHabitacion(201, TipoHabitacion.SUITE,  450.0);
            hotel.registrarCliente("12345678", "Juan",  "Pérez",  "999111222", "juan@mail.com");
            hotel.registrarCliente("87654321", "María", "García", "988333444", "maria@mail.com");
        } catch (Exception ignored) {}
    }

    // ── MAIN ──────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(HotelApp::new);
    }
}
