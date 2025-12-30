import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BirthdayCakeApp extends JPanel implements ActionListener {

    String flavor, icingColor, name;
    int age;

    Random random = new Random();
    Timer timer;

    class Confetti {
        int x, y, size, speed;
        Color color;
        boolean circle;
    }

    ArrayList<Confetti> confettiList = new ArrayList<>();

    public BirthdayCakeApp(String flavor, String icingColor, int age, String name) {
        this.flavor = flavor.toLowerCase();
        this.icingColor = icingColor.toLowerCase();
        this.age = age;
        this.name = name.toUpperCase();

        for (int i = 0; i < 100; i++) {
            Confetti c = new Confetti();
            c.x = random.nextInt(600);
            c.y = random.nextInt(600);
            c.size = random.nextInt(5) + 4;
            c.speed = random.nextInt(5) + 3;
            c.circle = random.nextBoolean();
            c.color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
            confettiList.add(c);
        }

        timer = new javax.swing.Timer(30, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        setBackground(Color.LIGHT_GRAY);

        drawTable(g2);
        drawCake(g2);
        drawBanner(g2);
        drawBalloons(g2);
        drawConfetti(g2);
    }

    private void drawTable(Graphics2D g) {
        g.setColor(new Color(139, 69, 19));
        g.fillRect(100, 440, 400, 40);
        g.fillRect(120, 480, 20, 80);
        g.fillRect(460, 480, 20, 80);
    }

    private void drawCake(Graphics2D g) {
        Color baseColor;
        switch (flavor) {
            case "chocolate": baseColor = new Color(123, 63, 0); break;
            case "pink": baseColor = Color.PINK; break;
            default: baseColor = Color.WHITE;
        }

        int[] widths = {300, 220, 150};
        int[] heights = {60, 50, 40};
        int y = 380;

        for (int i = 0; i < 3; i++) {
            g.setColor(i == 0 ? baseColor : Color.WHITE);
            g.fillRect(300 - widths[i] / 2, y - heights[i], widths[i], heights[i]);
            g.fillOval(300 - widths[i] / 2, y - heights[i] - 15, widths[i], 30);

            // icing balls
            g.setColor(parseColor(icingColor));
            for (int x = 300 - widths[i] / 2; x < 300 + widths[i] / 2; x += 20) {
                g.fillOval(x, y - heights[i] - 10, 10, 10);
            }

            y -= heights[i] - 10;
        }

        // Age candle
        g.setColor(Color.MAGENTA);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString(String.valueOf(age), 290, y - 20);

        // Flame
        g.setColor(Color.ORANGE);
        g.fillOval(295, y - 35, 10, 10);
    }

    private void drawBanner(Graphics2D g) {
        String msg = "HAPPY BIRTHDAY " + name;
        g.setFont(new Font("Arial", Font.BOLD, 16));

        int startX = 80;
        int endX = 520;
        int letters = msg.length();

        for (int i = 0; i < letters; i++) {
            int x = startX + i * (endX - startX) / (letters - 1);
            int y = 60 + (int) (20 * Math.sin(Math.PI * i / (letters - 1)));

            g.setColor(i % 2 == 0 ? Color.MAGENTA : Color.WHITE);
            g.fillPolygon(
                    new int[]{x - 15, x + 15, x},
                    new int[]{y, y, y + 40},
                    3
            );

            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(msg.charAt(i)), x - 5, y + 25);
        }
    }

    private void drawBalloons(Graphics2D g) {
        Color[] colors = {Color.YELLOW, Color.PINK, Color.MAGENTA};
        int[][] positions = {{50, 300}, {100, 350}, {80, 420}};

        for (int i = 0; i < positions.length; i++) {
            g.setColor(colors[i]);
            g.fillOval(positions[i][0], positions[i][1], 50, 60);
            g.setColor(Color.BLACK);
            g.drawLine(positions[i][0] + 25, positions[i][1] + 60, 130, 550);
        }
    }

    private void drawConfetti(Graphics2D g) {
        for (Confetti c : confettiList) {
            g.setColor(c.color);
            if (c.circle)
                g.fillOval(c.x, c.y, c.size, c.size);
            else
                g.fillRect(c.x, c.y, c.size, c.size);
        }
    }

    private Color parseColor(String color) {
        switch (color) {
            case "red": return Color.RED;
            case "blue": return Color.BLUE;
            case "green": return Color.GREEN;
            case "yellow": return Color.YELLOW;
            case "pink": return Color.PINK;
            case "purple": return Color.MAGENTA;
            default: return Color.WHITE;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Confetti c : confettiList) {
            c.y += c.speed;
            if (c.y > 600) {
                c.y = 0;
                c.x = random.nextInt(600);
            }
        }
        repaint();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter flavor (chocolate, vanilla, pink): ");
        String flavor = sc.nextLine();

        System.out.print("Enter icing color: ");
        String icing = sc.nextLine();

        System.out.print("Enter age: ");
        int age = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter name: ");
        String name = sc.nextLine();

        JFrame frame = new JFrame("Birthday Celebration");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BirthdayCakeApp(flavor, icing, age, name));
        frame.setVisible(true);
    }
}
