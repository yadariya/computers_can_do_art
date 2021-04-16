import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static int population_size = 5;
    static ArrayList<BufferedImage> population = new ArrayList<>(population_size);

    public static void main(String[] args) throws IOException {
        String input = "head.jpg";
        String white_input = "white.jpg"; //white image
        BufferedImage inputPicture = ImageIO.read(new File(input));
        BufferedImage whitePicture = ImageIO.read(new File(white_input));
//        BufferedImage reWhitePicture = resize(whitePicture, 512, 512);
        for (int i = 0; i < population_size; i++) {
            population.add(whitePicture);
        }
        int generations = 0;
        int minFitness = 100;
//        JLabel picLabel = new JLabel(new ImageIcon(population.get(3)));
//        JPanel jPanel = new JPanel();
//        jPanel.add(picLabel);
//        JFrame f = new JFrame();
//        f.setSize(new Dimension(inputPicture.getWidth(), inputPicture.getHeight()));
//        f.add(jPanel);
//        f.setVisible(true);
//        for (int i = 0; i < population.size(); i++) {
//            mutation(population.get((i)), inputPicture);
//        }
//        for (int i = 0; i < population.size(); i++) {
//            JLabel picLabel = new JLabel(new ImageIcon(population.get(i)));
//            JPanel jPanel = new JPanel();
//            jPanel.add(picLabel);
//            JFrame f = new JFrame();
//            f.setSize(new Dimension(inputPicture.getWidth(), inputPicture.getHeight()));
//            f.add(jPanel);
//            f.setVisible(true);
//        }


    }

    public static float fitness(BufferedImage image, BufferedImage input) {
        float percentage = 0;
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
            System.out.println(count);
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
