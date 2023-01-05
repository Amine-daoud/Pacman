package pacman;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Model extends JPanel implements ActionListener {

	private Dimension dimension;
    private final Font font = new Font("Bitstream", Font.BOLD, 18);
    private final Font font2 = new Font("Arial", Font.BOLD, 15);
    private boolean vivant = false;
    private boolean mort = false;

    private final int Taille = 24;
    private final int nombre = 17;
    private final int ecran_taille = nombre * Taille;
    private final int vitesse = 6;

    private int nombre_de_fantome = 5;
    private int vies, score;
    private int[] dx, dy;
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, vitesse_du_fantome;

    private Image coeur, fantome;
    private Image haut, bas, gauche,droite;

    private int pacman_x, pacman_y, pacmand_x, pacmand_y;
    private int req_dx, req_dy;

    private final short blocks[] = {
    	19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18,22,22,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16,20,20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16,20,20,
        17,  16, 16, 16,  16,  16, 16, 16, 16, 16, 16, 16, 16, 16, 16,20,20,
        17, 16, 16, 16, 16, 16, 16, 16, 24, 24, 24, 24, 24, 24, 16,20,20,
        17, 16, 16, 24, 16, 16, 16, 20, 0, 0, 0,  0,  0,   0, 17,20,21,
        17, 16, 20, 0, 17, 16, 16, 20, 0, 0, 0,  0,  0,   0, 17,20,21,
        17, 16, 20, 0, 17, 16, 16, 20, 0, 0, 0,  0,  0,   0, 17,20,21,
        17, 16, 20, 0, 17, 16, 16, 16, 18, 18, 18, 18, 18, 18, 16,20,20,
        17, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16,20,20,
        17, 16,  20, 0,  17,  16,  16, 16, 24, 24, 24, 24, 24, 16, 16,20,20,
        17, 16, 16, 18, 16, 16, 16, 20, 0, 0, 0, 0, 0, 17, 16,20,20,
        17, 16, 16, 16, 16, 24, 24, 28, 0, 19, 18, 18, 18, 16, 16,20,20,
        17, 16, 16, 16, 20, 0, 0, 0, 0, 17, 16, 16, 16, 16, 16,20,20,
        17, 16, 16, 16, 16, 18, 18, 22, 0, 17, 16, 16, 16, 16, 16,20,20,
        17, 16, 16, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 16, 16,20,20,
        25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24,28,28
    };

    private final int vitessefantome[] = {1, 2, 3, 4, 6, 8};

    private int vitesse_pacman = 3;
    private short[] ecran;
    private Timer timer;

    public Model() {

    	Images();
    	Variables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        debut_jeu();
    }
    
    
    private void Images() {
    	bas = new ImageIcon("/src/images/down.gif").getImage();
    	haut = new ImageIcon("/src/images/up.gif").getImage();
    	gauche = new ImageIcon("/src/images/left.gif").getImage();
    	droite = new ImageIcon("/src/images/right.gif").getImage();
        fantome = new ImageIcon("/src/images/ghost.gif").getImage();
        coeur = new ImageIcon("/src/images/heart.png").getImage();

    }
       private void Variables() {

        ecran = new short[nombre * nombre];
        dimension = new Dimension(400, 500);
        ghost_x = new int[6];
        ghost_dx = new int[6];
        ghost_y = new int[6];
        ghost_dy = new int[6];
        vitesse_du_fantome = new int[6];
        dx = new int[4];
        dy = new int[4];
        
        timer = new Timer(40, this);
        timer.start();
    }

    private void jeu(Graphics2D g2d) {

        if (mort) {

            mort();

        } else {

        	mouvement_pacman();
        	affichange_pacman(g2d);
            mouvement_de_fantome(g2d);
            checkMaze();
        }
    }

    private void affichage(Graphics2D g2d) {
 
    	String start = "appuyez sur entrer pour debuter ";
        g2d.setColor(new Color(255,204,51));
        g2d.setFont(font);
        g2d.drawString(start, (ecran_taille)/6, 200);
        // ecran d'acceuil on demande a l'utilisateur d'appuyez sur espace pour debuter
    }

    private void Score(Graphics2D g) {
        g.setFont(font2);
        g.setColor(Color.WHITE);
        String s = "Votre score est de : " + score;
        g.drawString(s, ecran_taille / 4 + 96, ecran_taille + 16);

        for (int i = 0; i < vies; i++) {
            g.drawImage(coeur, i * 28 + 8, ecran_taille + 1, this);
        }
    }

    private void checkMaze() {

        int i = 0;
        boolean finished = true;

        while (i < nombre * nombre && finished) {

            if ((ecran[i]) != 0) {
                finished = false;
            }

            i++;
        }

        if (finished) {

            score += 50;

            if (nombre_de_fantome < 6) {
            	nombre_de_fantome++;
            }

            if (vitesse_pacman < 4) {
            	vitesse_pacman++;
            }

            Labyrinthe();
        }
    }

    private void mort() {

    	vies--;

        if (vies == 0) {
            vivant = false;
        }

        continuer_jeu();
    }

    private void mouvement_de_fantome(Graphics2D g2d) {

        int pos;
        int count;

        for (int i = 0; i < nombre_de_fantome; i++) {
            if (ghost_x[i] % Taille == 0 && ghost_y[i] % Taille == 0) {
                pos = ghost_x[i] / Taille + nombre * (int) (ghost_y[i] / Taille);

                count = 0;

                if ((ecran[pos] & 1) == 0 && ghost_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((ecran[pos] & 2) == 0 && ghost_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((ecran[pos] & 4) == 0 && ghost_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((ecran[pos] & 8) == 0 && ghost_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((ecran[pos] & 15) == 15) {
                        ghost_dx[i] = 0;
                        ghost_dy[i] = 0;
                    } else {
                        ghost_dx[i] = -ghost_dx[i];
                        ghost_dy[i] = -ghost_dy[i];
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    ghost_dx[i] = dx[count];
                    ghost_dy[i] = dy[count];
                }

            }

            ghost_x[i] = ghost_x[i] + (ghost_dx[i] * vitesse_du_fantome[i]);
            ghost_y[i] = ghost_y[i] + (ghost_dy[i] * vitesse_du_fantome[i]);
            affichange_fantome(g2d, ghost_x[i] + 1, ghost_y[i] + 1);

            if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12)
                    && pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12)
                    && vivant) {

                mort = true;
            }
        }
    }

    private void affichange_fantome(Graphics2D g2d, int x, int y) {
    	g2d.drawImage(fantome, x, y, this);
        }

    private void mouvement_pacman() {

        int pos;
        short ch;

        if (pacman_x % Taille == 0 && pacman_y % Taille == 0) {
            pos = pacman_x / Taille + nombre * (int) (pacman_y / Taille);
            ch = ecran[pos];

            if ((ch & 16) != 0) {
                ecran[pos] = (short) (ch & 15);
                score++;
            }

            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                }
            }

            // Check for standstill
            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }
        } 
        pacman_x = pacman_x + vitesse * pacmand_x;
        pacman_y = pacman_y + vitesse * pacmand_y;
    }

    private void affichange_pacman(Graphics2D g2d) {

        if (req_dx == -1) {
        	g2d.drawImage(gauche, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dx == 1) {
        	g2d.drawImage(droite, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dy == -1) {
        	g2d.drawImage(haut, pacman_x + 1, pacman_y + 1, this);
        } else {
        	g2d.drawImage(bas, pacman_x + 1, pacman_y + 1, this);
        }
    }

    private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < ecran_taille; y += Taille) {
            for (x = 0; x < ecran_taille; x += Taille) {

                g2d.setColor(new Color(102,0,153));// on mets la couleur des block en mauve 
                g2d.setStroke(new BasicStroke(5));
                
                if ((blocks[i] == 0)) { 
                	g2d.fillRect(x, y, Taille, Taille);
                 }

                if ((ecran[i] & 1) != 0) { 
                    g2d.drawLine(x, y, x, y + Taille - 1);
                }

                if ((ecran[i] & 2) != 0) { 
                    g2d.drawLine(x, y, x + Taille - 1, y);
                }

                if ((ecran[i] & 4) != 0) { 
                    g2d.drawLine(x + Taille - 1, y, x + Taille - 1,
                            y + Taille - 1);
                }

                if ((ecran[i] & 8) != 0) { 
                    g2d.drawLine(x, y + Taille - 1, x + Taille - 1,
                            y + Taille - 1);
                }

                if ((ecran[i] & 16) != 0) { 
                    g2d.setColor(Color.BLUE);// la couleur des points en bleu
                    g2d.fillOval(x + 10, y + 10, 6, 6);
               }

                i++;
            }
        }
    }

    private void debut_jeu() {

    	vies = 3;
        score = 0;
        Labyrinthe();
        nombre_de_fantome = 5;
        vitesse_pacman = 3;
    }

    private void Labyrinthe() {

        int i;
        for (i = 0; i < nombre * nombre; i++) {
            ecran[i] = blocks[i];
        }

        continuer_jeu();
    }

    private void continuer_jeu() {

    	int dx = 1;
        int random;

        for (int i = 0; i < nombre_de_fantome; i++) {

            ghost_y[i] = 4 * Taille; //position de depart du fantome
            ghost_x[i] = 4 * Taille;
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (vitesse_pacman + 1));

            if (random > vitesse_pacman) {
                random = vitesse_pacman;
            }

            vitesse_du_fantome[i] = vitessefantome[random];
        }

        pacman_x = 7 * Taille;  //position de depart
        pacman_y = 11 * Taille;
        pacmand_x = 0;	
        pacmand_y = 0;
        req_dx = 0;	
        req_dy = 0;
        mort = false;
    }

 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, dimension.width, dimension.height);

        drawMaze(g2d);
        Score(g2d);

        if (vivant) {
            jeu(g2d);
        } else {
        	affichage(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }


    //controls
    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (vivant) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_SPACE && timer.isRunning()) {
                    vivant = false;
                } 
            } else {
                if (key == KeyEvent.VK_ENTER) {
                    vivant = true;
                    debut_jeu();
                }
            }
        }
}

	
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
