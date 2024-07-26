//  snake_2d  موجود بداخل المجلد  Main.java   هنا ذكرنا أن الملف
package snake_2d;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
 
// و بالتالي أصبح إنشاء كائن منه يمثل إنشاء نافذة JFrame يرث من الكلاس Main هنا جعلنا الكلاس
public class Main extends JFrame {
 
    // فقط createAndShowGUI() سيقوم الكونستركتور بإستدعاء الدالة Main عند إنشاء كائن من الكلاس
    public Main() {
        createAndShowGUI();
    }
 
    // هنا نضع كود إنشاء النافذة و محتوياتها
    private void createAndShowGUI() {
 
        // هنا قمنا بإنشاء كائن من الحاوية التي تحتوي على اللعبة
        GameDrawer gameDrawer = new GameDrawer();
 
        // هنا وضعنا الحاوية التي تحتوي على اللعبة في النافذة حتى تظهر بداخلها
        add(gameDrawer);
 
        // هنا قمنا بتحديد بعض خصائص النافذة و جعلناها مرئية
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
 
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // التي ستنشئ النافذة createAndShowGUI() و بالتالي سيتم إستدعاء الدالة Main هنا قمنا بإنشاء كائن من الكلاس
                new Main();
            }
        });
    }
 
}