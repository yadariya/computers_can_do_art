import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static int population_size = 5;
    private static ArrayList<BufferedImage> population = new ArrayList<>(population_size);

    public static void main(String[] args) throws IOException {
        String input = "head.jpg";
        String white_input = "white.jpg"; //white image
        BufferedImage inputPicture = ImageIO.read(new File(input));
        BufferedImage whitePicture = ImageIO.read(new File(white_input));
        for (int i = 0; i < population_size; i++) {
            population.add(whitePicture);
        }
        int generations = 0;
        int maxFitness = 40;
        BufferedImage answer = selection(generations, maxFitness, inputPicture);
        JLabel picLabel = new JLabel(new ImageIcon(answer));
        JPanel jPanel = new JPanel();
        jPanel.add(picLabel);
        JFrame f = new JFrame();
        f.setSize(new Dimension(inputPicture.getWidth(), inputPicture.getHeight()));
        f.add(jPanel);
        f.setVisible(true);
    }

    public static BufferedImage selection(int generation, int maxFitness, BufferedImage input) {
        while (generation != 500) {
            generation += 1;
            for (int i = 0; i < population.size() / 2; i++) {
                int k = (int) (Math.random() * (population.size() - 2));
                mutation(population.get(k), input);
            }
            for (int m = 0; m < population.size() / 4; m++) {
                int k = (int) (Math.random() * (population.size() - 2));
                int l = (int) (Math.random() * (population.size() - 2));
                crossover(population.get(k), population.get(l));
            }
            for (int p = 0; p < population.size() / 3; p++) {
                if (fitness(population.get(p), input) > maxFitness) {
                    population.remove(population.get(p));
                }
                maxFitness--;
            }
        }

        int minimal = 35;
        int minimal_id = 0;
        for (int i = 0; i < population.size(); i++) {
            if (fitness(population.get(i), input) < minimal) {
                minimal = fitness(population.get(i), input);
                minimal_id = i;
            }
        }

        return population.get(minimal_id);
    }

    public static int fitness(BufferedImage image, BufferedImage input) {
        int percentage = 0;
        // take buffer data from both image files //
        DataBuffer dbA = image.getData().getDataBuffer();
        int sizeA = dbA.getSize();
        DataBuffer dbB = input.getData().getDataBuffer();
        int sizeB = dbB.getSize();
        int count = 0;
        if (sizeA == sizeB) {
            for (int i = 0; i < sizeA; i++) {
                if (dbA.getElem(i) != dbB.getElem(i)) {
                    count = count + 1;
                }
            }
            percentage = (count * 100) / sizeA;
        }
        return percentage;
    }


    public static void mutation(BufferedImage image, BufferedImage input) {
        Font font = new Font("Oswald", Font.ITALIC, 10);
        Graphics g = image.getGraphics();
        for (int i = 0; i < 5; i++) {
            int x = (int) (Math.random() * 512);
            int y = (int) (Math.random() * 512);
            Color color = new Color(input.getRGB(x, y));
            g.setFont(font);
            g.setColor(color);
            g.drawString("Dariya", x, y);
        }
    }

    public static void crossover(BufferedImage base, BufferedImage overlay) {
        try {
            BufferedImage copyOfImage =
                    new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
            Graphics g = copyOfImage.createGraphics();
            g.drawImage(base, 0, 0, null);
            population.add(copyOfImage);
            Graphics2D g2d = base.createGraphics();
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
            int x = (base.getWidth() - overlay.getWidth()) / 2;
            int y = (base.getHeight() - overlay.getHeight()) / 2;
            g2d.drawImage(overlay, x, y, null);
            g2d.dispose();
            ImageIO.write(base, "jpg", new File("Blended.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }
}
