import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class VotarBotUI extends JFrame {

    private Timer timer;
    private int timerCount;
    private static List<Candidato> candidatos = new ArrayList<>();
    public static void vaciarUrna() {
    candidatos.clear(); 
    EstadisticasVotantesUI.vaciarVotantes(); 
}
   //Menu inicial
    public VotarBotUI() {

        setTitle("Votar Bot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300); 
        setLocationRelativeTo(null); 

        
        JLabel welcomeLabel = new JLabel("Bienvenido a Votar Bot");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER); 

        
        JButton votarButton = new JButton("Votar");
        votarButton.setFont(new Font("Arial", Font.PLAIN, 20)); 

       
        timerCount = 0;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerCount++;
                if (timerCount >= 5) {
                    timer.stop();
                    // Cerrar la ventana actual
                    dispose();
                    // Abrir la ventana del administrador
                    new AdminUI(VotarBotUI.this).setVisible(true);
                }
            }
        });

        votarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                timerCount = 0;
                timer.start();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (timerCount < 5) {
                    // Si el botón se suelta antes de 5 segundos, abrir la ventana Votante
                    timer.stop();
                    dispose();
                    new VotanteUI().setVisible(true);
                } else {
                    timer.stop();
                }
            }
        });

       
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout()); 

        
        panel.add(welcomeLabel, BorderLayout.NORTH);   
        panel.add(votarButton, BorderLayout.CENTER);

        
        getContentPane().add(panel);
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new VotarBotUI().setVisible(true);
            }
        });
    }

    public static List<Candidato> getCandidatos() {
        return candidatos;
    }

    public static void agregarCandidato(Candidato candidato) {
        candidatos.add(candidato);
    }
}

class AdminUI extends JFrame {

    private VotarBotUI votarBotUI;

    public AdminUI(VotarBotUI votarBotUI) {
        this.votarBotUI = votarBotUI;

        
        setTitle("Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200); 
        setLocationRelativeTo(null); 

        
        JButton ingresarButton = new JButton("Ingresar candidatos");
        JButton verEstadisticasButton = new JButton("Ver estadísticas");
        JButton vaciarUrnaButton = new JButton("Vaciar urna");
        JButton salirButton = new JButton("Salir");

        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new IngresarCandidatoUI().setVisible(true);
            }
        });

        verEstadisticasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EstadisticasCandidatosUI().setVisible(true);
                new EstadisticasVotantesUI().setVisible(true);
            }
        });

        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                votarBotUI.setVisible(true);
            }
        });
        vaciarUrnaButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        VotarBotUI.vaciarUrna();
        JOptionPane.showMessageDialog(AdminUI.this, "Urna vaciada correctamente.");
    }
});

        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1)); 
        panel.add(ingresarButton);
        panel.add(verEstadisticasButton);
        panel.add(vaciarUrnaButton);
        panel.add(salirButton);

        
        getContentPane().add(panel);
    }
}

class IngresarCandidatoUI extends JFrame {

    private static final int MAX_CANDIDATOS = 3;

    public IngresarCandidatoUI() {
        
        setTitle("Ingresar Candidato");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200); 
        setLocationRelativeTo(null); 

        
        JLabel nombreLabel = new JLabel("Nombre:");
        JTextField nombreField = new JTextField(20);
        JLabel edadLabel = new JLabel("Edad:");
        JTextField edadField = new JTextField(20);
        JLabel partidoLabel = new JLabel("Partido Político:");
        JTextField partidoField = new JTextField(20);

        JButton guardarButton = new JButton("Guardar");
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText();
                String edadText = edadField.getText();
                String partido = partidoField.getText();

                if (nombre.isEmpty() || edadText.isEmpty() || partido.isEmpty()) {
                    JOptionPane.showMessageDialog(IngresarCandidatoUI.this, "Todos los campos son obligatorios.");
                    return;
                }

                int edad;
                try {
                    edad = Integer.parseInt(edadText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(IngresarCandidatoUI.this, "La edad debe ser un número.");
                    return;
                }

                
                if (VotarBotUI.getCandidatos().size() >= MAX_CANDIDATOS) {
                    JOptionPane.showMessageDialog(IngresarCandidatoUI.this, "Se han ingresado los 3 candidatos.");
                    dispose();
                } else {
                    VotarBotUI.agregarCandidato(new Candidato(nombre, edad, partido));
                   
                    nombreField.setText("");
                    edadField.setText("");
                    partidoField.setText("");
                    JOptionPane.showMessageDialog(IngresarCandidatoUI.this, "Candidato guardado.");
                }
            }
        });

        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2)); 
        panel.add(nombreLabel);
        panel.add(nombreField);
        panel.add(edadLabel);
        panel.add(edadField);
        panel.add(partidoLabel);
        panel.add(partidoField);
        panel.add(new JLabel());
        panel.add(guardarButton);

        
        getContentPane().add(panel);
    }
}

class VotanteUI extends JFrame {

    
    private class VotanteData {
        String genero;
        int edad;
    }

    private VotanteData votanteData;

    public VotanteUI() {
        
        votanteData = new VotanteData();

       
        setTitle("Votante");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200); 
        setLocationRelativeTo(null); 

        
        JLabel nombreLabel = new JLabel("Nombre:");
        JTextField nombreField = new JTextField(20);
        JLabel generoLabel = new JLabel("Género:");
        JComboBox<String> generoComboBox = new JComboBox<>(new String[]{"Masculino", "Femenino"});
        JLabel edadLabel = new JLabel("Edad:");
        JTextField edadField = new JTextField(20);

        
        JButton guardarButton = new JButton("Guardar");
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String genero = (String) generoComboBox.getSelectedItem();
                String edadText = edadField.getText();

                if (genero == null || edadText.isEmpty()) {
                    JOptionPane.showMessageDialog(VotanteUI.this, "Todos los campos son obligatorios.");
                    return;
                }

                int edad;
                try {
                    edad = Integer.parseInt(edadText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(VotanteUI.this, "La edad debe ser un número.");
                    return;
                }

                votanteData.genero = genero;
                votanteData.edad = edad;

                EstadisticasVotantesUI.agregarVotante(new Votante(genero, edad));
                
                dispose();
                new VotarPorCandidatoUI().setVisible(true);
            }
        });

        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2)); 
        panel.add(nombreLabel);
        panel.add(nombreField);
        panel.add(generoLabel);
        panel.add(generoComboBox);
        panel.add(edadLabel);
        panel.add(edadField);
        panel.add(new JLabel()); 
        panel.add(guardarButton);

        
        getContentPane().add(panel);
    }
}

class VotarPorCandidatoUI extends JFrame {

    public VotarPorCandidatoUI() {
        
        setTitle("Votar por Candidato");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300); 
        setLocationRelativeTo(null); 

        
        List<Candidato> candidatos = VotarBotUI.getCandidatos();

        
        if (candidatos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay candidatos registrados.");
            dispose();
            new VotarBotUI().setVisible(true);
            return;
        }

       
        JLabel votarLabel = new JLabel("Votar por su candidato preferido:");
        votarLabel.setHorizontalAlignment(JLabel.CENTER); 
       
        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new GridLayout(candidatos.size(), 1));

        for (Candidato candidato : candidatos) {
            JButton candidatoButton = new JButton(candidato.getNombre() + " - " + candidato.getPartidoPolitico());
            botonesPanel.add(candidatoButton);

            
            candidatoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                   
                    new MedioOrientacionUI(candidato).setVisible(true);
                    dispose();
                }
            });
        }

       
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(votarLabel, BorderLayout.NORTH);
        panel.add(botonesPanel, BorderLayout.CENTER);

        
        getContentPane().add(panel);
    }
}

class MedioOrientacionUI extends JFrame {

    public MedioOrientacionUI(Candidato candidato) {
       
        setTitle("Medio de Orientación");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); 

       
        JLabel orientacionLabel = new JLabel("¿Qué medio influyó en su decisión?");
        orientacionLabel.setHorizontalAlignment(JLabel.CENTER); 

        
        JButton televisionButton = new JButton("Televisión");
        JButton radioButton = new JButton("Radio");
        JButton internetButton = new JButton("Internet");

        televisionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                candidato.incrementarVotos();
                candidato.incrementarTelevision();
                JOptionPane.showMessageDialog(MedioOrientacionUI.this, "Su voto ha sido registrado.");
                dispose();
                new VotarBotUI().setVisible(true);
            }
        });

        radioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                candidato.incrementarVotos();
                candidato.incrementarRadio();
                JOptionPane.showMessageDialog(MedioOrientacionUI.this, "Su voto ha sido registrado.");
                dispose();
                new VotarBotUI().setVisible(true);
            }
        });

        internetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                candidato.incrementarVotos();
                candidato.incrementarInternet();
                JOptionPane.showMessageDialog(MedioOrientacionUI.this, "Su voto ha sido registrado.");
                dispose();
                new VotarBotUI().setVisible(true);
            }
        });

        
        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new GridLayout(3, 1));
        botonesPanel.add(televisionButton);
        botonesPanel.add(radioButton);
        botonesPanel.add(internetButton);

        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(orientacionLabel, BorderLayout.NORTH);
        panel.add(botonesPanel, BorderLayout.CENTER);

       
        getContentPane().add(panel);
    }
}


class EstadisticasCandidatosUI extends JFrame {

    public EstadisticasCandidatosUI() {
       
        setTitle("Estadísticas de Candidatos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 300); 
        setLocationRelativeTo(null); 

       
        List<Candidato> candidatos = VotarBotUI.getCandidatos();

        
        JLabel estadisticasLabel = new JLabel("Estadísticas de los Candidatos:");
        estadisticasLabel.setHorizontalAlignment(JLabel.CENTER); 

        
        JPanel estadisticasPanel = new JPanel();
        estadisticasPanel.setLayout(new GridLayout(candidatos.size(), 1));

        for (Candidato candidato : candidatos) {
            int costoCampania = candidato.getTelevision() * 1000 + candidato.getRadio() * 500 + candidato.getInternet() * 100;

            JLabel candidatoLabel = new JLabel(
                "Nombre: " + candidato.getNombre() +
                ", Partido: " + candidato.getPartidoPolitico() +
                ", Votos: " + candidato.getVotos() +
                ", TV: " + candidato.getTelevision() +
                ", Radio: " + candidato.getRadio() +
                ", Internet: " + candidato.getInternet() +
                ", Costo Campaña: $" + costoCampania
            );
            estadisticasPanel.add(candidatoLabel);
        }

        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(estadisticasLabel, BorderLayout.NORTH);
        panel.add(estadisticasPanel, BorderLayout.CENTER);

        
        getContentPane().add(panel);
    }
}


class EstadisticasVotantesUI extends JFrame {

    private static List<Votante> votantes = new ArrayList<>();

    public EstadisticasVotantesUI() {
        
        setTitle("Estadísticas de Votantes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300); 
        setLocationRelativeTo(null); 

        
        JLabel estadisticasLabel = new JLabel("Estadísticas de los Votantes:");
        estadisticasLabel.setHorizontalAlignment(JLabel.CENTER);

       
        int totalVotantes = votantes.size();
        int hombres = 0;
        int mujeres = 0;
        Map<Integer, Integer> votosPorEdad = new HashMap<>();

        for (Votante votante : votantes) {
            if (votante.getGenero().equals("Masculino")) {
                hombres++;
            } else {
                mujeres++;
            }

            int edad = votante.getEdad();
            votosPorEdad.put(edad, votosPorEdad.getOrDefault(edad, 0) + 1);
        }

        double porcentajeHombres = (hombres * 100.0) / totalVotantes;
        double porcentajeMujeres = (mujeres * 100.0) / totalVotantes;

     
        JPanel estadisticasPanel = new JPanel();
        estadisticasPanel.setLayout(new GridLayout(votosPorEdad.size() + 4, 1));

        JLabel totalVotantesLabel = new JLabel("Total de votantes: " + totalVotantes);
        estadisticasPanel.add(totalVotantesLabel);

        JLabel porcentajeHombresLabel = new JLabel("Porcentaje de hombres: " + String.format("%.2f", porcentajeHombres) + "%");
        estadisticasPanel.add(porcentajeHombresLabel);

        JLabel porcentajeMujeresLabel = new JLabel("Porcentaje de mujeres: " + String.format("%.2f", porcentajeMujeres) + "%");
        estadisticasPanel.add(porcentajeMujeresLabel);

        estadisticasPanel.add(new JLabel("Votos por edad:"));

        for (Map.Entry<Integer, Integer> entry : votosPorEdad.entrySet()) {
            int edad = entry.getKey();
            int votos = entry.getValue();
            JLabel votosEdadLabel = new JLabel("Edad " + edad + ": " + votos + " votos");
            estadisticasPanel.add(votosEdadLabel);
        }

      
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(estadisticasLabel, BorderLayout.NORTH);
        panel.add(estadisticasPanel, BorderLayout.CENTER);

        
        getContentPane().add(panel);
    }

  
    public static void agregarVotante(Votante votante) {
        votantes.add(votante);
    }

    
    public static void vaciarVotantes() {
        votantes.clear();
    }
}

class Candidato {

    private String nombre;
    private int edad;
    private String partidoPolitico;
    private int votos;
    private int television;
    private int radio;
    private int internet;

    public Candidato(String nombre, int edad, String partidoPolitico) {
        this.nombre = nombre;
        this.edad = edad;
        this.partidoPolitico = partidoPolitico;
        this.votos = 0;
        this.television = 0;
        this.radio = 0;
        this.internet = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public String getPartidoPolitico() {
        return partidoPolitico;
    }

    public int getVotos() {
        return votos;
    }

    public int getTelevision() {
        return television;
    }

    public int getRadio() {
        return radio;
    }

    public int getInternet() {
        return internet;
    }

    public void incrementarVotos() {
        votos++;
    }

    public void incrementarTelevision() {
        television++;
    }

    public void incrementarRadio() {
        radio++;
    }

    public void incrementarInternet() {
        internet++;
    }
}

class Votante {

    private String genero;
    private int edad;

    public Votante(String genero, int edad) {
        this.genero = genero;
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public int getEdad() {
        return edad;
    }
}















