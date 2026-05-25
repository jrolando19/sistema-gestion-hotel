package hotel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.*;
import java.util.List;

public class HotelApp extends JFrame {

    private final GestionHotel hotel = new GestionHotel();
    private final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── Paleta ────────────────────────────────────────────────────────────────
    private static final Color C_SIDEBAR      = new Color(26, 39, 68);
    private static final Color C_SIDEBAR_ACT  = new Color(49, 68, 115);
    private static final Color C_SIDEBAR_HOV  = new Color(38, 54, 92);
    private static final Color C_SIDEBAR_LOGO = new Color(17, 27, 52);
    private static final Color C_ACCENT_BAR   = new Color(99, 155, 255);   // barra izquierda activa
    private static final Color C_BG           = new Color(244, 246, 252);
    private static final Color C_CARD         = Color.WHITE;
    private static final Color C_PRIMARY      = new Color(37, 99, 235);
    private static final Color C_PRIMARY_DARK = new Color(29, 78, 216);
    private static final Color C_SUCCESS      = new Color(22, 163, 74);
    private static final Color C_SUCCESS_DARK = new Color(15, 130, 58);
    private static final Color C_DANGER       = new Color(220, 38, 38);
    private static final Color C_DANGER_DARK  = new Color(185, 28, 28);
    private static final Color C_GRAY         = new Color(107, 114, 128);
    private static final Color C_GRAY_LIGHT   = new Color(209, 213, 219);
    private static final Color C_BORDER       = new Color(229, 231, 235);
    private static final Color C_TEXT         = new Color(17, 24, 39);
    private static final Color C_TEXT_SUBTLE  = new Color(75, 85, 99);
    private static final Color C_WHITE        = Color.WHITE;
    private static final Color C_TABLE_ALT    = new Color(249, 250, 255);
    private static final Color C_TABLE_SEL    = new Color(219, 234, 254);
    private static final Color C_TABLE_HEAD   = new Color(30, 58, 138);
    private static final Color C_PLACEHOLDER  = new Color(180, 188, 204);
    private static final Color C_FOCUS        = new Color(37, 99, 235);

    // ── Fuentes ───────────────────────────────────────────────────────────────
    private static final Font F_TITLE    = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font F_BODY     = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font F_SMALL    = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font F_TINY     = new Font("Segoe UI", Font.PLAIN, 10);
    private static final Font F_H1       = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font F_H2       = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font F_METRIC   = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font F_MONO     = new Font("Consolas", Font.PLAIN, 12);
    private static final Font F_PH       = new Font("Segoe UI", Font.ITALIC, 12);

    // ── Estado de navegacion ──────────────────────────────────────────────────
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel      = new JPanel(cardLayout);
    private JButton activeNavBtn        = null;
    private JLabel  headerPageTitle     = new JLabel("Habitaciones");

    // ── Tablas con datos demo ─────────────────────────────────────────────────
    private JTable tablaHabitaciones;
    private JTable tablaClientes;

    // ── Status bar labels ─────────────────────────────────────────────────────
    private JLabel statusHabs;
    private JLabel statusClients;
    private JLabel statusReservas;

    public HotelApp() {
        setTitle("Sistema de Gestion de Hotel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 740);
        setMinimumSize(new Dimension(960, 620));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildSidebar(),    BorderLayout.WEST);
        add(buildMainArea(),   BorderLayout.CENTER);
        add(buildStatusBar(),  BorderLayout.SOUTH);

        cargarDatosDemo();
        refrescarHabitaciones(tablaHabitaciones);
        refrescarClientes(tablaClientes);
        actualizarStatusBar();

        setVisible(true);
    }

    // ── SIDEBAR ───────────────────────────────────────────────────────────────

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(C_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(215, 0));

        // Logo
        JPanel logoPanel = new JPanel(new BorderLayout(12, 0));
        logoPanel.setBackground(C_SIDEBAR_LOGO);
        logoPanel.setBorder(new EmptyBorder(18, 16, 18, 16));
        logoPanel.setMaximumSize(new Dimension(215, 76));

        JPanel logoSquare = new JPanel(new BorderLayout());
        logoSquare.setBackground(C_PRIMARY);
        logoSquare.setPreferredSize(new Dimension(38, 38));
        logoSquare.setBorder(new RoundedBorder(8, C_PRIMARY_DARK));
        JLabel logoInitial = new JLabel("H", SwingConstants.CENTER);
        logoInitial.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logoInitial.setForeground(C_WHITE);
        logoSquare.add(logoInitial);

        JPanel logoTexts = new JPanel();
        logoTexts.setLayout(new BoxLayout(logoTexts, BoxLayout.Y_AXIS));
        logoTexts.setBackground(C_SIDEBAR_LOGO);
        JLabel nameLabel = new JLabel("Hotel Manager");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(C_WHITE);
        JLabel versionLabel = new JLabel("Sistema de Gestion");
        versionLabel.setFont(F_TINY);
        versionLabel.setForeground(new Color(148, 163, 184));
        logoTexts.add(nameLabel);
        logoTexts.add(Box.createVerticalStrut(2));
        logoTexts.add(versionLabel);

        logoPanel.add(logoSquare, BorderLayout.WEST);
        logoPanel.add(logoTexts,  BorderLayout.CENTER);
        sidebar.add(logoPanel);

        // Separador
        sidebar.add(sidebarSep());
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(sidebarSectionLabel("MODULOS"));

        // Items de navegacion
        Object[][] navItems = {
            {"Habitaciones", "habitaciones", statusHabs = new JLabel("3")},
            {"Clientes",     "clientes",     statusClients = new JLabel("2")},
            {"Reservas",     "reservas",     statusReservas = new JLabel("0")},
            {"Check-in/out", "checkinout",   null},
            {"Facturas",     "facturas",     null},
        };

        JButton firstBtn = null;
        for (Object[] item : navItems) {
            JLabel badge = (JLabel) item[2];
            JButton btn = buildNavButton((String) item[0], (String) item[1], badge);
            if (firstBtn == null) firstBtn = btn;
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(2));
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(sidebarSep());

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        footer.setBackground(C_SIDEBAR_LOGO);
        footer.setMaximumSize(new Dimension(215, 44));
        JLabel footerLbl = new JLabel("(c) 2025 Sistema Hotelero");
        footerLbl.setFont(F_TINY);
        footerLbl.setForeground(new Color(100, 116, 139));
        footer.add(footerLbl);
        sidebar.add(footer);

        if (firstBtn != null) {
            activeNavBtn = firstBtn;
            firstBtn.setBackground(C_SIDEBAR_ACT);
            firstBtn.setForeground(C_WHITE);
        }
        return sidebar;
    }

    private JPanel sidebarSep() {
        JPanel sep = new JPanel();
        sep.setBackground(new Color(255, 255, 255, 18));
        sep.setMaximumSize(new Dimension(215, 1));
        sep.setPreferredSize(new Dimension(215, 1));
        return sep;
    }

    private JLabel sidebarSectionLabel(String text) {
        JLabel lbl = new JLabel("  " + text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(new Color(100, 116, 139));
        lbl.setMaximumSize(new Dimension(215, 28));
        lbl.setPreferredSize(new Dimension(215, 28));
        lbl.setOpaque(true);
        lbl.setBackground(C_SIDEBAR);
        return lbl;
    }

    private JButton buildNavButton(String label, String cardName, JLabel countBadge) {
        JButton btn = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fondo redondeado
                g2.setColor(getBackground());
                g2.fillRoundRect(8, 2, getWidth() - 16, getHeight() - 4, 8, 8);
                // Barra de acento izquierda (solo si activo)
                if (this == activeNavBtn) {
                    g2.setColor(C_ACCENT_BAR);
                    g2.fillRoundRect(0, 8, 4, getHeight() - 16, 4, 4);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(C_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(215, 44));
        btn.setPreferredSize(new Dimension(215, 44));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Badge de conteo a la derecha
        if (countBadge != null) {
            countBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
            countBadge.setForeground(new Color(148, 163, 184));
            countBadge.setHorizontalAlignment(SwingConstants.RIGHT);
            btn.setLayout(new BorderLayout());
            JLabel lblText = new JLabel("  " + label);
            lblText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblText.setForeground(btn.getForeground());
            btn.add(lblText, BorderLayout.WEST);
            btn.add(countBadge, BorderLayout.EAST);
            btn.setBorder(new EmptyBorder(10, 14, 10, 14));
            btn.setText("");

            // Sincronizar color del texto con el botón
            btn.addActionListener(e -> lblText.setForeground(C_WHITE));
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (btn != activeNavBtn) lblText.setForeground(new Color(229, 234, 245));
                }
                public void mouseExited(MouseEvent e) {
                    if (btn != activeNavBtn) lblText.setForeground(new Color(203, 213, 225));
                }
            });
        } else {
            btn.setBorder(new EmptyBorder(10, 18, 10, 14));
            btn.setText("  " + label);
        }

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn != activeNavBtn) btn.setBackground(C_SIDEBAR_HOV);
            }
            public void mouseExited(MouseEvent e) {
                if (btn != activeNavBtn) btn.setBackground(C_SIDEBAR);
            }
        });

        btn.addActionListener(e -> {
            if (activeNavBtn != null) {
                activeNavBtn.setBackground(C_SIDEBAR);
                activeNavBtn.setForeground(new Color(203, 213, 225));
            }
            btn.setBackground(C_SIDEBAR_ACT);
            btn.setForeground(C_WHITE);
            activeNavBtn = btn;
            headerPageTitle.setText(label);
            cardLayout.show(mainPanel, cardName);
        });
        return btn;
    }

    // ── AREA PRINCIPAL ────────────────────────────────────────────────────────

    private JPanel buildMainArea() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(C_BG);

        // Header con titulo dinamico + fecha
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(C_WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, C_BORDER),
            new EmptyBorder(12, 24, 12, 24)));

        headerPageTitle.setFont(F_H1);
        headerPageTitle.setForeground(C_TEXT);

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        headerRight.setBackground(C_WHITE);

        // Pastilla de fecha
        JPanel datePill = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        datePill.setBackground(new Color(239, 246, 255));
        datePill.setBorder(new RoundedBorder(8, new Color(191, 219, 254)));
        try {
            String fecha = LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy",
                    new java.util.Locale("es", "PE")));
            JLabel dateLbl = new JLabel(fecha);
            dateLbl.setFont(F_SMALL);
            dateLbl.setForeground(C_PRIMARY);
            datePill.add(dateLbl);
        } catch (Exception ignored) {}
        headerRight.add(datePill);

        header.add(headerPageTitle, BorderLayout.WEST);
        header.add(headerRight,     BorderLayout.EAST);

        mainPanel.setBackground(C_BG);
        mainPanel.add(buildHabitacionesPanel(), "habitaciones");
        mainPanel.add(buildClientesPanel(),     "clientes");
        mainPanel.add(buildReservasPanel(),     "reservas");
        mainPanel.add(buildCheckPanel(),        "checkinout");
        mainPanel.add(buildFacturasPanel(),     "facturas");

        wrapper.add(header,    BorderLayout.NORTH);
        wrapper.add(mainPanel, BorderLayout.CENTER);
        return wrapper;
    }

    // ── STATUS BAR ────────────────────────────────────────────────────────────

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        bar.setBackground(new Color(248, 249, 251));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, C_BORDER));

        bar.add(statusPill("Habitaciones", "3", C_PRIMARY));
        bar.add(statusPill("Clientes",     "2", C_SUCCESS));
        bar.add(statusPill("Reservas",     "0", new Color(124, 58, 237)));

        JLabel system = new JLabel("Sistema de Gestion de Hotel  v1.0");
        system.setFont(F_TINY);
        system.setForeground(C_GRAY_LIGHT);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        right.setBackground(new Color(248, 249, 251));
        right.add(system);
        // Necesitamos un layout que coloque el right a la derecha
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(248, 249, 251));
        wrapper.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, C_BORDER));
        wrapper.add(bar,   BorderLayout.WEST);
        wrapper.add(right, BorderLayout.EAST);
        return wrapper;
    }

    private JPanel statusPill(String label, String value, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        p.setBackground(new Color(248, 249, 251));
        JLabel dot = new JLabel("●");
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        dot.setForeground(color);
        JLabel lbl = new JLabel(label + ": " + value);
        lbl.setFont(F_TINY);
        lbl.setForeground(C_GRAY);
        p.add(dot);
        p.add(lbl);
        return p;
    }

    private void actualizarStatusBar() {
        if (statusHabs    != null) statusHabs.setText(String.valueOf(hotel.listarHabitaciones().size()));
        if (statusClients != null) statusClients.setText(String.valueOf(hotel.listarClientes().size()));
        if (statusReservas != null) statusReservas.setText(String.valueOf(hotel.listarReservas().size()));
    }

    // ── TOAST ─────────────────────────────────────────────────────────────────

    private void showToast(String msg, boolean success) {
        Color bg = success ? C_SUCCESS : C_DANGER;
        JWindow toast = new JWindow(this);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        panel.setBackground(bg);
        panel.setBorder(new RoundedBorder(10, bg.darker()));
        JLabel lbl = new JLabel((success ? "[OK] " : "[!] ") + msg);
        lbl.setFont(F_BODY);
        lbl.setForeground(C_WHITE);
        panel.add(lbl);
        toast.add(panel);
        toast.pack();
        Point loc = getLocationOnScreen();
        toast.setLocation(loc.x + getWidth() - toast.getWidth() - 24,
                          loc.y + getHeight() - toast.getHeight() - 60);
        toast.setVisible(true);
        new Timer(2800, ev -> toast.dispose()).start();
    }

    private void ok(String msg)  { showToast(msg, true); }
    private void err(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }

    // ── UTILIDADES ────────────────────────────────────────────────────────────

    /** Card blanca con borde redondeado, sombra suave y título */
    private JPanel card(String titulo) {
        JPanel shadow = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                for (int i = 4; i >= 0; i--) {
                    g2.setColor(new Color(0, 0, 0, 6 - i));
                    g2.fillRoundRect(i, i + 2, getWidth() - i * 2, getHeight() - i * 2, 14, 14);
                }
                g2.setColor(C_CARD);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 12, 12);
                g2.dispose();
            }
        };
        shadow.setOpaque(false);
        shadow.setBorder(new EmptyBorder(0, 0, 4, 4));

        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(C_CARD);
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(18, 20, 18, 20));

        if (!titulo.isEmpty()) {
            JLabel lbl = new JLabel(titulo);
            lbl.setFont(F_H2);
            lbl.setForeground(C_TEXT);
            lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
            p.add(lbl, BorderLayout.NORTH);
        }
        shadow.add(p);
        return shadow;
    }

    /** Devuelve el JPanel interno de la card para agregar contenido */
    private JPanel cardBody(JPanel cardPanel) {
        // El shadow panel contiene el inner panel
        if (cardPanel instanceof JPanel) {
            for (Component c : cardPanel.getComponents()) {
                if (c instanceof JPanel) return (JPanel) c;
            }
        }
        return cardPanel;
    }

    private JPanel pagePanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(C_BG);
        p.setBorder(new EmptyBorder(22, 22, 22, 22));
        return p;
    }

    private JLabel formLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(C_GRAY);
        return l;
    }

    /** TextField con placeholder y borde azul al hacer foco */
    private JTextField field(int cols, String placeholder) {
        JTextField f = new JTextField(cols) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner() && !placeholder.isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(C_PLACEHOLDER);
                    g2.setFont(F_PH);
                    Insets ins = getInsets();
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(placeholder, ins.left + 2,
                        ins.top + fm.getAscent() + (getHeight() - fm.getHeight()) / 2);
                    g2.dispose();
                }
            }
        };
        f.setFont(F_BODY);
        f.setBackground(C_WHITE);
        f.setForeground(C_TEXT);
        f.setCaretColor(C_PRIMARY);
        setBorderNormal(f);
        f.setPreferredSize(new Dimension(f.getPreferredSize().width, 34));

        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { setBorderFocus(f); }
            public void focusLost(FocusEvent e)   { setBorderNormal(f); }
        });
        return f;
    }

    private void setBorderNormal(JTextField f) {
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER, 1),
            new EmptyBorder(6, 10, 6, 10)));
    }

    private void setBorderFocus(JTextField f) {
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_FOCUS, 2),
            new EmptyBorder(5, 9, 5, 9)));
    }

    private <T> JComboBox<T> combo(T[] items) {
        JComboBox<T> cb = new JComboBox<>(items);
        cb.setFont(F_BODY);
        cb.setBackground(C_WHITE);
        cb.setForeground(C_TEXT);
        cb.setBorder(BorderFactory.createLineBorder(C_BORDER, 1));
        cb.setPreferredSize(new Dimension(cb.getPreferredSize().width + 20, 34));
        return cb;
    }

    /** Grupo label + field apilados verticalmente */
    private JPanel fieldGroup(String labelText, JComponent field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(C_CARD);
        p.setOpaque(false);
        JLabel lbl = formLabel(labelText);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 1, 4, 0));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(field);
        return p;
    }

    // Separador fino horizontal dentro de una card
    private JPanel divider() {
        JPanel d = new JPanel();
        d.setBackground(C_BORDER);
        d.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        d.setPreferredSize(new Dimension(0, 1));
        d.setOpaque(true);
        return d;
    }

    private JButton primaryBtn(String text) { return makeBtn(text, C_PRIMARY, C_PRIMARY_DARK); }
    private JButton successBtn(String text) { return makeBtn(text, C_SUCCESS, C_SUCCESS_DARK); }
    private JButton dangerBtn(String text)  { return makeBtn(text, C_DANGER,  C_DANGER_DARK);  }

    private JButton makeBtn(String text, Color c1, Color c2) {
        JButton b = new JButton(text) {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color top = hovered ? c1.brighter() : c1;
                Color bot = hovered ? c2.brighter() : c2;
                g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bot));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setForeground(C_WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        b.setPreferredSize(new Dimension(b.getPreferredSize().width + 12, 34));
        return b;
    }

    /** Boton secundario (outline) */
    private JButton outlineBtn(String text, Color color) {
        JButton b = new JButton(text) {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hovered) {
                    g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 18));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setForeground(color);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(7, 14, 7, 14));
        b.setPreferredSize(new Dimension(b.getPreferredSize().width + 12, 34));
        return b;
    }

    /** Tabla con zebra stripes, header forzado y mensaje de empty state */
    private JTable makeTable(String[] cols, String emptyMsg) {
        DefaultTableModel m = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable t = new JTable(m) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? C_WHITE : C_TABLE_ALT);
                    c.setForeground(C_TEXT);
                    if (c instanceof JLabel) ((JLabel)c).setBorder(new EmptyBorder(0, 12, 0, 12));
                }
                return c;
            }
            @Override public void paint(Graphics g) {
                super.paint(g);
                if (getRowCount() == 0) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(C_GRAY_LIGHT);
                    g2.setFont(new Font("Segoe UI", Font.ITALIC, 13));
                    FontMetrics fm = g2.getFontMetrics();
                    String msg = emptyMsg;
                    int x = (getWidth() - fm.stringWidth(msg)) / 2;
                    int y = Math.max(60, getHeight() / 2);
                    g2.drawString(msg, x, y);
                    g2.dispose();
                }
            }
        };
        t.setFont(F_BODY);
        t.setRowHeight(38);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionBackground(C_TABLE_SEL);
        t.setSelectionForeground(C_TEXT);
        t.setFillsViewportHeight(true);
        t.setBorder(BorderFactory.createEmptyBorder());

        // Header renderer propio — ignora el L&F del sistema
        t.getTableHeader().setDefaultRenderer((table, value, isSel, hasFocus, row, col) -> {
            JLabel label = new JLabel(value != null ? value.toString() : "");
            label.setFont(new Font("Segoe UI", Font.BOLD, 11));
            label.setForeground(C_WHITE);
            label.setBackground(C_TABLE_HEAD);
            label.setOpaque(true);
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(55, 85, 175)),
                new EmptyBorder(0, 12, 0, 12)));
            return label;
        });
        t.getTableHeader().setPreferredSize(new Dimension(0, 40));
        t.getTableHeader().setReorderingAllowed(false);
        t.getTableHeader().setResizingAllowed(true);
        return t;
    }

    private DefaultTableModel model(JTable t) { return (DefaultTableModel) t.getModel(); }

    /** ScrollPane con borde integrado y contador de filas debajo */
    private JPanel tableWithCount(JTable t) {
        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(new RoundedBorder(8, C_BORDER));
        sp.getViewport().setBackground(C_WHITE);

        JLabel countLbl = new JLabel(t.getRowCount() + " registros");
        countLbl.setFont(F_TINY);
        countLbl.setForeground(C_GRAY);
        countLbl.setBorder(new EmptyBorder(6, 2, 0, 0));

        // Actualizar conteo cuando cambia el modelo
        t.getModel().addTableModelListener(e -> {
            int n = t.getRowCount();
            countLbl.setText(n + " registro" + (n != 1 ? "s" : ""));
        });

        JPanel wrapper = new JPanel(new BorderLayout(0, 0));
        wrapper.setOpaque(false);
        wrapper.add(sp,       BorderLayout.CENTER);
        wrapper.add(countLbl, BorderLayout.SOUTH);
        return wrapper;
    }

    // ── HABITACIONES ─────────────────────────────────────────────────────────

    private JPanel buildHabitacionesPanel() {
        JPanel page = pagePanel();

        // Formulario
        JPanel formCard = card("Registrar nueva habitacion");
        JPanel body = cardBody(formCard);

        JTextField fNum    = field(7,  "Ej: 101");
        JComboBox<TipoHabitacion> fTipo = combo(TipoHabitacion.values());
        JTextField fPrecio = field(10, "Ej: 150.00");

        JPanel fields = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        fields.setOpaque(false);
        fields.add(fieldGroup("NUMERO DE HABITACION", fNum));
        fields.add(fieldGroup("TIPO DE HABITACION",   fTipo));
        fields.add(fieldGroup("PRECIO POR NOCHE (S/.)", fPrecio));

        JButton btnAgregar = primaryBtn("+ Agregar habitacion");
        JButton btnRefresh = outlineBtn("Actualizar lista", C_SUCCESS);

        btnAgregar.setToolTipText("Registrar una nueva habitacion en el sistema");
        btnRefresh.setToolTipText("Recargar la lista de habitaciones");

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botonesPanel.setOpaque(false);
        botonesPanel.add(btnRefresh);
        botonesPanel.add(btnAgregar);

        JPanel divRow = new JPanel(new BorderLayout());
        divRow.setOpaque(false);
        divRow.add(divider(), BorderLayout.CENTER);
        divRow.setBorder(new EmptyBorder(12, 0, 10, 0));

        body.add(fields,    BorderLayout.CENTER);
        body.add(divRow,    BorderLayout.SOUTH);

        // Adjuntar botones al bottom del card (fuera del body que ya ocupa CENTER/SOUTH)
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionRow.setBackground(C_CARD);
        actionRow.setOpaque(false);
        actionRow.add(btnRefresh);
        actionRow.add(btnAgregar);

        // Rebuild body layout
        body.removeAll();
        body.add(fields,    BorderLayout.CENTER);
        body.add(actionRow, BorderLayout.SOUTH);

        // Tabla
        String[] cols = {"N Hab.", "Tipo", "Precio / Noche", "Estado"};
        tablaHabitaciones = makeTable(cols, "No hay habitaciones registradas");
        tablaHabitaciones.getColumnModel().getColumn(3).setCellRenderer(new StatusBadgeRenderer());
        tablaHabitaciones.getColumnModel().getColumn(0).setPreferredWidth(80);
        tablaHabitaciones.getColumnModel().getColumn(1).setPreferredWidth(140);
        tablaHabitaciones.getColumnModel().getColumn(2).setPreferredWidth(160);

        JPanel tableCard = card("Listado de habitaciones");
        cardBody(tableCard).add(tableWithCount(tablaHabitaciones), BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> {
            try {
                int num = Integer.parseInt(fNum.getText().trim());
                double precio = Double.parseDouble(fPrecio.getText().trim());
                TipoHabitacion tipo = (TipoHabitacion) fTipo.getSelectedItem();
                hotel.registrarHabitacion(num, tipo, precio);
                refrescarHabitaciones(tablaHabitaciones);
                fNum.setText(""); fPrecio.setText(""); fNum.requestFocus();
                actualizarStatusBar();
                ok("Habitacion " + num + " registrada correctamente.");
            } catch (NumberFormatException ex) {
                err("El numero y el precio deben ser valores numericos.");
            } catch (Exception ex) { err(ex.getMessage()); }
        });

        // Enter en fPrecio dispara el boton
        fPrecio.addActionListener(e -> btnAgregar.doClick());

        btnRefresh.addActionListener(e -> refrescarHabitaciones(tablaHabitaciones));

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(C_BG);
        content.add(formCard,  BorderLayout.NORTH);
        content.add(tableCard, BorderLayout.CENTER);
        page.add(content, BorderLayout.CENTER);
        return page;
    }

    private void refrescarHabitaciones(JTable t) {
        model(t).setRowCount(0);
        for (Habitacion h : hotel.listarHabitaciones()) {
            model(t).addRow(new Object[]{
                h.getNumero(), h.getTipo(),
                String.format("S/. %.2f", h.getPrecioPorNoche()),
                h.getEstado().toString()
            });
        }
    }

    // ── CLIENTES ─────────────────────────────────────────────────────────────

    private JPanel buildClientesPanel() {
        JPanel page = pagePanel();

        JPanel formCard = card("Registrar nuevo cliente");
        JPanel body = cardBody(formCard);

        JTextField fDni   = field(10, "Ej: 12345678");
        JTextField fNom   = field(12, "Nombre");
        JTextField fApe   = field(12, "Apellido");
        JTextField fTel   = field(10, "Ej: 999000111");
        JTextField fEmail = field(16, "correo@mail.com");

        JPanel fields = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        fields.setOpaque(false);
        fields.add(fieldGroup("DNI", fDni));
        fields.add(fieldGroup("NOMBRE", fNom));
        fields.add(fieldGroup("APELLIDO", fApe));
        fields.add(fieldGroup("TELEFONO", fTel));
        fields.add(fieldGroup("EMAIL", fEmail));

        JButton btnReg     = primaryBtn("+ Registrar cliente");
        JButton btnRefresh = outlineBtn("Actualizar lista", C_SUCCESS);

        btnReg.setToolTipText("Registrar un nuevo cliente en el sistema");
        btnRefresh.setToolTipText("Recargar la lista de clientes");

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionRow.setOpaque(false);
        actionRow.add(btnRefresh);
        actionRow.add(btnReg);

        body.add(fields,    BorderLayout.CENTER);
        body.add(actionRow, BorderLayout.SOUTH);

        String[] cols = {"DNI", "Nombre", "Apellido", "Telefono", "Email"};
        tablaClientes = makeTable(cols, "No hay clientes registrados");

        JPanel tableCard = card("Listado de clientes");
        cardBody(tableCard).add(tableWithCount(tablaClientes), BorderLayout.CENTER);

        btnReg.addActionListener(e -> {
            try {
                hotel.registrarCliente(
                    fDni.getText().trim(), fNom.getText().trim(),
                    fApe.getText().trim(), fTel.getText().trim(),
                    fEmail.getText().trim());
                refrescarClientes(tablaClientes);
                fDni.setText(""); fNom.setText(""); fApe.setText("");
                fTel.setText(""); fEmail.setText(""); fDni.requestFocus();
                actualizarStatusBar();
                ok("Cliente registrado correctamente.");
            } catch (Exception ex) { err(ex.getMessage()); }
        });

        fEmail.addActionListener(e -> btnReg.doClick());
        btnRefresh.addActionListener(e -> refrescarClientes(tablaClientes));

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(C_BG);
        content.add(formCard,  BorderLayout.NORTH);
        content.add(tableCard, BorderLayout.CENTER);
        page.add(content, BorderLayout.CENTER);
        return page;
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
        JPanel page = pagePanel();

        JPanel formCard = card("Crear nueva reserva");
        JPanel body = cardBody(formCard);

        JTextField fDni     = field(10, "DNI del cliente");
        JTextField fHab     = field(7,  "N de habitacion");
        JTextField fEntrada = field(10, "dd/MM/yyyy");
        JTextField fSalida  = field(10, "dd/MM/yyyy");

        JPanel fields = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        fields.setOpaque(false);
        fields.add(fieldGroup("DNI CLIENTE",           fDni));
        fields.add(fieldGroup("N HABITACION",          fHab));
        fields.add(fieldGroup("FECHA ENTRADA",         fEntrada));
        fields.add(fieldGroup("FECHA SALIDA",          fSalida));

        JButton btnCrear    = primaryBtn("+ Crear reserva");
        JButton btnCancelar = dangerBtn("Cancelar seleccionada");
        JButton btnRefresh  = outlineBtn("Actualizar lista", C_SUCCESS);

        btnCrear.setToolTipText("Crear una nueva reserva con los datos ingresados");
        btnCancelar.setToolTipText("Cancelar la reserva seleccionada en la tabla");
        btnRefresh.setToolTipText("Recargar la lista de reservas");

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionRow.setOpaque(false);
        actionRow.add(btnRefresh);
        actionRow.add(btnCancelar);
        actionRow.add(btnCrear);

        body.add(fields,    BorderLayout.CENTER);
        body.add(actionRow, BorderLayout.SOUTH);

        String[] cols = {"ID Reserva", "Cliente", "Habitacion", "Entrada", "Salida", "Noches", "Costo", "Estado"};
        JTable tabla = makeTable(cols, "No hay reservas registradas");
        tabla.getColumnModel().getColumn(7).setCellRenderer(new StatusBadgeRenderer());
        tabla.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(100);

        JPanel tableCard = card("Listado de reservas");
        cardBody(tableCard).add(tableWithCount(tabla), BorderLayout.CENTER);

        btnCrear.addActionListener(e -> {
            try {
                LocalDate entrada = LocalDate.parse(fEntrada.getText().trim(), FMT);
                LocalDate salida  = LocalDate.parse(fSalida.getText().trim(), FMT);
                int numHab = Integer.parseInt(fHab.getText().trim());
                Reserva r = hotel.crearReserva(fDni.getText().trim(), numHab, entrada, salida);
                refrescarReservas(tabla);
                fDni.setText(""); fHab.setText(""); fEntrada.setText(""); fSalida.setText("");
                fDni.requestFocus();
                actualizarStatusBar();
                ok("Reserva creada: " + r.getId());
            } catch (DateTimeParseException ex) {
                err("Formato de fecha incorrecto. Use dd/MM/yyyy");
            } catch (Exception ex) { err(ex.getMessage()); }
        });

        btnCancelar.addActionListener(e -> {
            int row = tabla.getSelectedRow();
            if (row < 0) { err("Seleccione una reserva en la tabla para cancelarla."); return; }
            String id = (String) model(tabla).getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Cancelar la reserva " + id + "?", "Confirmar cancelacion",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;
            try {
                hotel.cancelarReserva(id);
                refrescarReservas(tabla);
                actualizarStatusBar();
                ok("Reserva " + id + " cancelada.");
            } catch (Exception ex) { err(ex.getMessage()); }
        });

        btnRefresh.addActionListener(e -> refrescarReservas(tabla));

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(C_BG);
        content.add(formCard,  BorderLayout.NORTH);
        content.add(tableCard, BorderLayout.CENTER);
        page.add(content, BorderLayout.CENTER);
        return page;
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
                r.getEstado().toString()
            });
        }
    }

    // ── CHECK-IN / CHECK-OUT ─────────────────────────────────────────────────

    private JPanel buildCheckPanel() {
        JPanel page = pagePanel();

        JPanel formCard = card("Gestionar entrada y salida");
        JPanel body = cardBody(formCard);

        JTextField fId = field(16, "Ej: RES-001");

        // Ayuda contextual
        JLabel hint = new JLabel("Ingrese el ID de la reserva y seleccione la operacion a realizar.");
        hint.setFont(F_SMALL);
        hint.setForeground(C_GRAY);
        hint.setBorder(new EmptyBorder(0, 0, 8, 0));

        JPanel fields = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        fields.setOpaque(false);
        fields.add(fieldGroup("ID DE RESERVA", fId));

        JButton btnCI = successBtn("Check-in");
        JButton btnCO = primaryBtn("Check-out + Factura");

        btnCI.setToolTipText("Registrar la llegada del huesped (activa la reserva)");
        btnCO.setToolTipText("Registrar la salida y generar la factura");

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actionRow.setOpaque(false);
        actionRow.add(btnCI);
        actionRow.add(btnCO);

        JPanel topSection = new JPanel(new BorderLayout(0, 10));
        topSection.setOpaque(false);
        topSection.add(hint,     BorderLayout.NORTH);
        topSection.add(fields,   BorderLayout.CENTER);
        topSection.add(actionRow, BorderLayout.SOUTH);

        body.add(topSection, BorderLayout.CENTER);

        // Log con JTextPane — fondo oscuro tipo terminal
        JTextPane log = new JTextPane();
        log.setFont(F_MONO);
        log.setEditable(false);
        log.setBackground(new Color(13, 18, 36));
        log.setBorder(new EmptyBorder(14, 16, 14, 16));
        StyledDocument doc = log.getStyledDocument();

        Style styleCI    = doc.addStyle("checkin",  null);
        StyleConstants.setForeground(styleCI, new Color(52, 211, 153));
        StyleConstants.setBold(styleCI, true);

        Style styleCO    = doc.addStyle("checkout", null);
        StyleConstants.setForeground(styleCO, new Color(96, 165, 250));
        StyleConstants.setBold(styleCO, true);

        Style styleInfo  = doc.addStyle("info", null);
        StyleConstants.setForeground(styleInfo, new Color(203, 213, 225));

        Style styleMoney = doc.addStyle("money", null);
        StyleConstants.setForeground(styleMoney, new Color(251, 191, 36));
        StyleConstants.setBold(styleMoney, true);

        Style styleSep   = doc.addStyle("sep", null);
        StyleConstants.setForeground(styleSep, new Color(30, 41, 59));

        // Mensaje inicial en el log
        appendLog(doc, "  Sistema de registro de operaciones\n", "info");
        appendLog(doc, "  Ingrese un ID de reserva y presione Check-in o Check-out\n\n", "sep");

        JPanel logCard = card("Registro de operaciones");
        JScrollPane logScroll = new JScrollPane(log);
        logScroll.setBorder(new RoundedBorder(8, new Color(30, 41, 59)));
        logScroll.getViewport().setBackground(new Color(13, 18, 36));
        cardBody(logCard).add(logScroll, BorderLayout.CENTER);

        btnCI.addActionListener(e -> {
            try {
                String id = fId.getText().trim();
                if (id.isEmpty()) { err("Ingrese el ID de la reserva."); return; }
                hotel.realizarCheckIn(id);
                appendLog(doc, "\n  CHECK-IN ----------------------------------------\n", "checkin");
                appendLog(doc, "  Reserva : " + id + "\n", "checkin");
                appendLog(doc, "  Estado  : ACTIVA - Huesped registrado\n", "checkin");
                appendLog(doc, "  ------------------------------------------------\n", "sep");
                fId.setText(""); fId.requestFocus();
                actualizarStatusBar();
                ok("Check-in realizado correctamente para reserva " + id);
            } catch (Exception ex) { err(ex.getMessage()); }
        });

        btnCO.addActionListener(e -> {
            try {
                String id = fId.getText().trim();
                if (id.isEmpty()) { err("Ingrese el ID de la reserva."); return; }
                Factura f = hotel.realizarCheckOut(id);
                appendLog(doc, "\n  CHECK-OUT ---------------------------------------\n", "checkout");
                appendLog(doc, "  Reserva  : " + id + "\n", "info");
                appendLog(doc, "  Factura  : " + f.getNumeroFactura() + "\n", "info");
                appendLog(doc, "  Cliente  : " + f.getReserva().getCliente().getNombreCompleto() + "\n", "info");
                appendLog(doc, "  Hab.     : " + f.getReserva().getHabitacion().getNumero() + "\n", "info");
                appendLog(doc, "  Noches   : " + f.getReserva().getNochesReservadas() + "\n", "info");
                appendLog(doc, "  Subtotal : S/. " + String.format("%.2f", f.getSubtotal()) + "\n", "info");
                appendLog(doc, "  IGV 18%  : S/. " + String.format("%.2f", f.getIgv())      + "\n", "info");
                appendLog(doc, "  TOTAL    : S/. " + String.format("%.2f", f.getTotal())     + "\n", "money");
                appendLog(doc, "  ------------------------------------------------\n", "sep");
                fId.setText(""); fId.requestFocus();
                actualizarStatusBar();
                ok("Check-out completado. Factura: " + f.getNumeroFactura());
            } catch (Exception ex) { err(ex.getMessage()); }
        });

        // Enter activa check-in
        fId.addActionListener(e -> btnCI.doClick());

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(C_BG);
        content.add(formCard, BorderLayout.NORTH);
        content.add(logCard,  BorderLayout.CENTER);
        page.add(content, BorderLayout.CENTER);
        return page;
    }

    private void appendLog(StyledDocument doc, String text, String styleName) {
        try { doc.insertString(doc.getLength(), text, doc.getStyle(styleName)); }
        catch (BadLocationException ignored) {}
    }

    // ── FACTURAS ─────────────────────────────────────────────────────────────

    private JPanel buildFacturasPanel() {
        JPanel page = pagePanel();

        // Tarjetas de metricas
        JLabel metricCount = new JLabel("0", SwingConstants.CENTER);
        JLabel metricTotal = new JLabel("S/. 0.00", SwingConstants.CENTER);
        JLabel metricAvg   = new JLabel("S/. 0.00", SwingConstants.CENTER);

        JPanel metricsRow = new JPanel(new GridLayout(1, 3, 14, 0));
        metricsRow.setBackground(C_BG);
        metricsRow.setOpaque(false);
        metricsRow.add(metricCard("Total facturas emitidas", metricCount, C_PRIMARY));
        metricsRow.add(metricCard("Total recaudado",         metricTotal, C_SUCCESS));
        metricsRow.add(metricCard("Promedio por estadia",    metricAvg,
                                   new Color(124, 58, 237)));

        // Tabla
        String[] cols = {"N Factura", "Cliente", "Reserva", "Noches", "Subtotal", "IGV", "Total", "Emision"};
        JTable tabla = makeTable(cols, "No hay facturas generadas. Realice un Check-out primero.");

        JButton btnRefresh = outlineBtn("Actualizar lista", C_SUCCESS);
        btnRefresh.setToolTipText("Recargar el historial de facturas y actualizar metricas");
        btnRefresh.addActionListener(e -> {
            refrescarFacturas(tabla);
            actualizarMetricas(metricCount, metricTotal, metricAvg);
        });

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionRow.setOpaque(false);
        actionRow.add(btnRefresh);

        JPanel tableCard = card("Historial de facturas");
        cardBody(tableCard).add(actionRow,              BorderLayout.NORTH);
        cardBody(tableCard).add(tableWithCount(tabla),  BorderLayout.CENTER);

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(C_BG);
        content.add(metricsRow, BorderLayout.NORTH);
        content.add(tableCard,  BorderLayout.CENTER);
        page.add(content, BorderLayout.CENTER);
        return page;
    }

    private JPanel metricCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel shadow = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                for (int i = 3; i >= 0; i--) {
                    g2.setColor(new Color(0, 0, 0, 5 - i));
                    g2.fillRoundRect(i, i + 2, getWidth() - i * 2, getHeight() - i * 2, 14, 14);
                }
                g2.setColor(C_CARD);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 12, 12);
                g2.dispose();
            }
        };
        shadow.setOpaque(false);
        shadow.setBorder(new EmptyBorder(0, 0, 4, 4));

        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(C_CARD);
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Franja de color superior
        JPanel accentStrip = new JPanel();
        accentStrip.setBackground(accentColor);
        accentStrip.setPreferredSize(new Dimension(0, 4));
        accentStrip.setBorder(new RoundedBorder(4, accentColor));

        JPanel inner = new JPanel(new BorderLayout(0, 6));
        inner.setOpaque(false);
        inner.setBorder(new EmptyBorder(16, 20, 18, 20));

        JLabel titleLbl = new JLabel(title.toUpperCase());
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        titleLbl.setForeground(C_GRAY);

        valueLabel.setFont(F_METRIC);
        valueLabel.setForeground(accentColor);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);

        inner.add(titleLbl,   BorderLayout.NORTH);
        inner.add(valueLabel, BorderLayout.CENTER);

        p.add(accentStrip, BorderLayout.NORTH);
        p.add(inner,       BorderLayout.CENTER);
        shadow.add(p);
        return shadow;
    }

    private void actualizarMetricas(JLabel count, JLabel total, JLabel avg) {
        List<Factura> facturas = hotel.listarFacturas();
        int n = facturas.size();
        double sum = facturas.stream().mapToDouble(Factura::getTotal).sum();
        count.setText(String.valueOf(n));
        total.setText(String.format("S/. %.2f", sum));
        avg.setText(String.format("S/. %.2f", n > 0 ? sum / n : 0));
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

    // ── STATUS BADGE RENDERER ─────────────────────────────────────────────────

    static class StatusBadgeRenderer extends JPanel implements TableCellRenderer {
        private final JLabel label = new JLabel();

        StatusBadgeRenderer() {
            setLayout(new GridBagLayout());
            label.setFont(new Font("Segoe UI", Font.BOLD, 11));
            label.setOpaque(true);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int col) {
            String estado = value != null ? value.toString() : "";
            label.setText("  " + estado + "  ");

            Color fg, bg;
            switch (estado.toUpperCase()) {
                case "DISPONIBLE": fg = new Color(21, 128, 61);  bg = new Color(220, 252, 231); break;
                case "OCUPADA":    fg = new Color(185, 28, 28);  bg = new Color(254, 226, 226); break;
                case "PENDIENTE":  fg = new Color(146, 64, 14);  bg = new Color(254, 243, 199); break;
                case "ACTIVA":     fg = new Color(29, 78, 216);  bg = new Color(219, 234, 254); break;
                case "FINALIZADA": fg = new Color(55, 65, 81);   bg = new Color(243, 244, 246); break;
                case "CANCELADA":  fg = new Color(185, 28, 28);  bg = new Color(254, 226, 226); break;
                default:           fg = new Color(75, 85, 99);   bg = new Color(243, 244, 246);
            }

            label.setForeground(fg);
            label.setBackground(bg);
            label.setBorder(new RoundedBorder(10, fg));

            Color panelBg = isSelected ? C_TABLE_SEL
                                       : (row % 2 == 0 ? Color.WHITE : new Color(249, 250, 255));
            setBackground(panelBg);
            setOpaque(true);
            return this;
        }
    }

    // ── ROUNDED BORDER ────────────────────────────────────────────────────────

    static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        RoundedBorder(int radius, Color color) { this.radius = radius; this.color = color; }

        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }
        @Override public Insets getBorderInsets(Component c, Insets i) {
            i.set(radius / 2, radius / 2, radius / 2, radius / 2); return i;
        }
    }

    // ── DATOS DEMO ────────────────────────────────────────────────────────────

    private void cargarDatosDemo() {
        try {
            hotel.registrarHabitacion(101, TipoHabitacion.SIMPLE, 120.0);
            hotel.registrarHabitacion(102, TipoHabitacion.DOBLE,  200.0);
            hotel.registrarHabitacion(201, TipoHabitacion.SUITE,  450.0);
            hotel.registrarCliente("12345678", "Juan",  "Perez",  "999111222", "juan@mail.com");
            hotel.registrarCliente("87654321", "Maria", "Garcia", "988333444", "maria@mail.com");
        } catch (Exception ignored) {}
    }

    // ── MAIN ──────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(HotelApp::new);
    }
}
