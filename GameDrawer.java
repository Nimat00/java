//  snake_2d  موجود بداخل المجلد  GameDrawer.java   هنا ذكرنا أن الملف
package snake_2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
 
// أي أصبح هذا الكلاس يمثل حاوية ,JPanel هنا قمنا بإنشاء كلاس يرث من الكلاس
// حتى نجعل المستخدم قادر على تحريك الأفعى من الكيبورد KeyListener و جعلناه يطبق الإنترفيس
// حتى نستطيع رسم الأفعى من جديد كلما تحركت ActionListener و جعلناه يطبق الإنترفيس
public class GameDrawer extends JPanel implements KeyListener, ActionListener{
 
    // قمنا بإنشاء هاتين المصفوفتين لتخزين مكان وجود كل دائرة من الأفعى كل لحظة, أي لتحديد المكان المحجوز لعرض دوائر الأفعى
    // ( بما أن عدد الدوائر الأقصى بالطول هو 22 و بالعرض هو أيضاً 22, فهذا يعني أنه يمكن تخزين 22×22 دائرة ( أي 484
    final int[] boardX = new int[484];
    final int[] boardY = new int[484];
 
    // سنستخدم هذه المصفوفة لتخزين مكان وجود كل دائرة في الأفعى حتى نعرف الموقع الذي لا يجب أن نظهر فيه الدائرة الحمراء
    // ملاحظة: سبب إستخدام هذا المصفوفة لتخزين موقع كل دائرة في الأفعى من جديد هو فقط لجعل اللعبة لا تعلق, أي لتحسين أداء اللعبة
    LinkedList<Position> snake = new LinkedList();
 
    // سنستخدم هذه المتغيرات لتحديد الإتجاه الذي ستتجه إليه الأفعى
    boolean left = false;
    boolean right = false;
    boolean up = false;
    boolean down = false;
 
    // سنستخدم هذه الكائنات لرسم إتجاه وجه الأفعى
    ImageIcon lookToRightImage = new ImageIcon(this.getClass().getResource("/images/face-look-right.jpg"));
    ImageIcon lookToLeftImage = new ImageIcon(this.getClass().getResource("/images/face-look-left.jpg"));
    ImageIcon lookToUpImage = new ImageIcon(this.getClass().getResource("/images/face-look-up.jpg"));
    ImageIcon lookToDownImage = new ImageIcon(this.getClass().getResource("/images/face-look-down.jpg"));
 
    // سنستخدم هذا الكائن في كل مرة لرسم جسد الأفعى
    ImageIcon snakeBodyImage = new ImageIcon(this.getClass().getResource("/images/body.png"));
 
    // سنستخدم هذا الكائن في كل مرة لرسم طعام الأفعى
    ImageIcon fruitImage = new ImageIcon(this.getClass().getResource("/images/fruit.png"));
 
    // سنستخدم هذا المتغير لتخزين عدد الدوائر التي تشكل الأفعى
    int lengthOfSnake = 3;
 
    // سنستخدم هاتين المصفوفتين لتحديد الأماكن التي يمكن أن يظهر فيها الطعام
    int[] fruitXPos = {20, 40, 60, 80, 100, 120, 140, 160, 200, 220, 240, 260, 280, 300, 320, 340, 360, 380, 400, 420, 440, 460};
    int[] fruitYPos = {20, 40, 60, 80, 100, 120, 140, 160, 200, 220, 240, 260, 280, 300, 320, 340, 360, 380, 400, 420, 440, 460};
 
    // سنستخدم هذا الكائن لجعل الأفعى تستمر بالحركة .Thread و الذي يشبه الـ Timer هنا قمنا بإنشاء كائن من الكلاس
    Timer speedTimer;
 
    // سنستخدم هذا المتغير لتحديد أنه كل 0.1 ثانية سستتحرك الأفعى
    int delay = 100;
 
    // سنستخدم هذا المتغير لمعرفة إذا كانت الأفعى تتحرك أم لا
    int moves = 0;
 
    // سنستخدم هذه المتغيرات لتحديد المجموع الذي يحققه اللاعب أثناء اللعب
    int totalScore = 0;
    int fruitEaten = 0;
    int scoreReverseCounter = 99;
 
    // في حال كان اللاعب قد حقق مجموع عالي من قبل, سيتم إظهاره كأفضل مجموع وصل إليه
    int bestScore = readBestScorefromTheFile();
 
    // لتوليد أماكن ظهور طعام الأفعى بشكل عشوائي random سنستخدم الكائن
    Random random = new Random();
 
    // هنا قمنا بتحديد مكان أول طعام سيظهر في اللعبة قبل أن يبدأ المستخدم باللعب, و جعلناه يظهر تحت الأفعى
    int xPos = random.nextInt(22);
    int yPos = 5+random.nextInt(17);
 
    // سنستخدم هذا المتغير لمعرفة ما إذا كان المستخدم قد خسر أم لا
    boolean isGameOver = false;
 
    // هنا تحديد حجم اللعبة و تهيئتها لإستشعار الأزرار التي ينقر عليها المستخدم لحظة إنشاء كائن من هذا الكلاس
    // لجعل اللعبة تتحرك بسرعة 0.1 بالثانية speedTimer و قمنا تهيئة اللعبة لإستشعار الأزرار التي ينقر عليها المستخدم و تهيئة الكائن
    public GameDrawer()
    {
        setPreferredSize(new Dimension(750, 500));
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        speedTimer = new Timer(delay, this);
    }
 
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }
 
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
    }
 
    // (لتحديد كيف سيتم رسم و تلوين كل شيء في الحاوية (أي في اللعبة paint() للدالة Override هنا فعلنا
    // ملاحظة هذه الدالة تستدعى بشكل تلقائي عند رسم أي شيء في الحاوية
    @Override
    public void paint(Graphics g)
    {
        // هنا قمنا بتحديد مكان وجود الأفعى في كل مرة يقوم المستخدم ببدأ اللعبة من جديد
        if(moves == 0)
        {
            boardX[2] = 40;
            boardX[1] = 60;
            boardX[0] = 80;
 
            boardY[2] = 100;
            boardY[1] = 100;
            boardY[0] = 100;
 
            scoreReverseCounter = 99;
            speedTimer.start();
        }
 
        // هنا قمنا بجعل المجموع الحالي الذي وصل إليه المستخدم يظهر كأعلى مجموع وصل إليه في حال تخطى المجموع القديم
        if(totalScore > bestScore)
            bestScore = totalScore;
 
        // هنا قمن بإنشاء مربع أسود يمثل لون خلفية اللعبة
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 750, 500);
 
        // هنا قمنا برسم المربعات التي تشكل الحدود التي لا تستطيع الأفعى عبورها باللون الرمادي
        // حجم كل مربع 13 بيكسل و المسافة بينهما 5 بيكسل
        g.setColor(Color.DARK_GRAY);
        for(int i=6; i<=482; i+=17)
            for(int j=6; j<=482; j+=17)
                g.fillRect(i, j, 13, 13);
 
        // هنا فمنا بإنشاء مربع أسود كبير فوق المربعات التي تشكل حدود اللعبة لتظهر و كأنها فارغة من الداخل
        g.setColor(Color.BLACK);
        g.fillRect(20, 20, 460, 460);
 
        // هنا قمنا بكتابة إسم اللعبة و تلوينه بالأزرق
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.drawString("Snake 2D", 565, 35);
 
        // هنا قمنا بكتابة إسم اللعبة و تلوينه بالأزرق
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        g.drawString("+ "+scoreReverseCounter, 510, 222);
 
        // هنا جعلنا أي شيء سنقوم بكتابته يظهر باللون الرمادي
        g.setColor(Color.LIGHT_GRAY);
 
        // هنا قمنا بطباعة أنه تم تطوير اللعبة بواسطة موقعنا
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawString("Developed by harmash.com", 530, 60);
 
        // لقياس حجم الأرفام الظاهرة في المربعات الثلاث بالبيكسل حتى نستطيع عرضهم في الوسط FontMetrics هنا قمنا بإنشاء الكائن
        FontMetrics fm = g.getFontMetrics();
 
        // هنا جعلنا أي شيء سنقوم بكتابته يظهر بنوع و حجم هذا الخط
        g.setFont(new Font("Arial", Font.PLAIN, 18));
 
        //و المربع الذي تحته و الرقم الذي بداخله Best Score هنا قمنا برسم النص
        g.drawString("Best Score", 576, 110);
        g.drawRect(550, 120, 140, 30);
        g.drawString(bestScore+"", 550+(142-fm.stringWidth(bestScore+""))/2, 142);
 
        //و المربع الذي تحته و الرقم الذي بداخله Total Score هنا قمنا برسم النص
        g.drawString("Total Score", 573, 190);
        g.drawRect(550, 200, 140, 30);
        g.drawString(totalScore+"", 550+(142-fm.stringWidth(totalScore+""))/2, 222);
 
        //و المربع الذي تحته و الرقم الذي بداخله Fruit Eaten هنا قمنا برسم النص
        g.drawString("Fruit Eaten", 575, 270);
        g.drawRect(550, 280, 140, 30);
        g.drawString(fruitEaten+"", 550+(142-fm.stringWidth(fruitEaten+""))/2, 302);
 
        // Controls هنا قمنا برسم النص
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Controls", 550, 360);
 
        // Controls هنا قمنا برسم النصوص الظاهرة تحت النص
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Pause / Start : Space", 550, 385);
        g.drawString("lookTo Up : Arrow Up", 550, 410);
        g.drawString("lookTo Down : Arrow Down", 550, 435);
        g.drawString("lookTo Left : Arrow Left", 550, 460);
        g.drawString("lookTo Right : Arrow Right", 550, 485);
 
        // هنا جعلنا الأفعى تنظر ناحية اليمين قبل أن يبدأ اللاعب بتحريكها
        lookToRightImage.paintIcon(this, g, boardX[0], boardY[0]);
 
        // هنا قمنا بمسح مكان وجود الأفعى السابق لأننا سنقوم بتخزين المكان الجديد كلما تحركت
        snake.clear();
 
        // هنا قمنا بإنشاء حلقة ترسم كامل الدوائر التي تشكل الأفعى كل 0.1 ثانية
        for(int i=0; i<lengthOfSnake; i++)
        {
            if(i==0 && left)
                lookToLeftImage.paintIcon(this, g, boardX[i], boardY[i]);
 
            else if(i==0 && right)
                lookToRightImage.paintIcon(this, g, boardX[i], boardY[i]);
 
            else if(i==0 && up)
                lookToUpImage.paintIcon(this, g, boardX[i], boardY[i]);
 
            else if(i==0 && down)
                lookToDownImage.paintIcon(this, g, boardX[i], boardY[i]);
 
            else if(i!=0)
                snakeBodyImage.paintIcon(this, g, boardX[i], boardY[i]);
 
            // snake هنا قمنا بتخزين الموقع الحالي لكل دائرة في الأفعى في الكائن
            snake.add(new Position(boardX[i], boardY[i]));
        }
 
        // تقل بشكل تدريجي و أدنى قيمة ممكن أن تصل إليها هي 10 scoreReverseCounter هنا جعلنا قيمة العداد
        if(scoreReverseCounter != 10)
            scoreReverseCounter--;
 
        // هنا قمنا بإنشاء هذه الحلقة للتأكد إذا كان رأس الأفعى قد لامس أي جزء من جسدها
        for(int i=1; i<lengthOfSnake; i++)
        {
            // إذاً عندما يلمس رأس الأفعى جسدها سيتم جعل أول دائرة موجودة خلف الرأس تمثل رأس الأفعى حتى لا يظهر رأسها فوق جسدها
            if(boardX[i] == boardX[0] && boardY[i] == boardY[0])
            {
                if(right)
                    lookToRightImage.paintIcon(this, g, boardX[1], boardY[1]);
 
                else if(left)
                    lookToLeftImage.paintIcon(this, g, boardX[1], boardY[1]);
 
                else if(up)
                    lookToUpImage.paintIcon(this, g, boardX[1], boardY[1]);
 
                else if(down)
                    lookToDownImage.paintIcon(this, g, boardX[1], boardY[1]);
 
                // للإشارة إلى أن اللاعب قد خسر true تساوي isGameOver بعدها سيتم جعل قيمة الـ
                // Space و بالتالي يمكنه أن يبدأ من جديد بالنقر على زر المسافة الفارغة
                isGameOver = true;
 
                // يتوقف و بالتالي ستتوقف الأفعى تماماً عن الحركة speedTimer بعدها سيتم جعل الـ
                speedTimer.stop();
 
                // Game Over بعدها سيتم إظهار النص
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.drawString("Game Over", 110, 220);
 
                // تحته Press Space To Restart و سيتم إظهار النص
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Press Space To Restart", 130, 260);
 
                // في الأخير سيتم إستدعاء هذه الدالة لحفظ أكبر مجموع وصل إليه اللاعب
                writeBestScoreInTheFile();
            }
        }
 
        // إذا لمس رأس الأفعى الطعام سيتم إخفاء الطعام و زيادة مجموع اللاعب
        if((fruitXPos[xPos] == boardX[0]) && fruitYPos[yPos] == boardY[0])
        {
            totalScore += scoreReverseCounter;
            scoreReverseCounter = 99;
            fruitEaten++;
            lengthOfSnake++;
        }
 
        // هنا في كل مرة سيتم ضمان أن لا يظهر الطعام فوق الأفعى
        for(int i=0; i<snake.size(); i++)
        {
            // في حال ظهر الطعام فوق جسد الأفعى سيتم خلق مكان عشوائي آخر لوضعها فيه
            if(snake.get(i).x == fruitXPos[xPos] && snake.get(i).y == fruitYPos[yPos]) {
                xPos = random.nextInt(22);
                yPos = random.nextInt(22);
            }
        }
 
        // في الأخير سيتم عرض الطعام بعيداً عن جسد الأفعى
        fruitImage.paintIcon(this, g, fruitXPos[xPos], fruitYPos[yPos]);
 
        // قمنا باستدعاء هذه الدالة للتخلص من أي مصادر لا حاجة لها في البرنامج و يمكنك إزالتها لأنها لن تؤثر هنا
        g.dispose();
    }
 
 
    // لتحديد الإتجاه الذي ستتحرك فيه النافذة keyPressed() للدالة Override هنا فعلنا
    @Override
    public void keyPressed(KeyEvent e) {
 
        // Space هنا قمنا بتحديد ما سيحدث إذا قام اللاعب بالنقر على زر المسافة الفارغة
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
 
            // سيتم إيقاف اللعبة بشكل مؤقت إذا كانت اللعبة شغالة
            if(speedTimer.isRunning() && isGameOver == false)
                speedTimer.stop();
 
            // سيتم إعادة اللعبة للعمل إذا كان قد تم إيقافها سابقاً
            else if(!speedTimer.isRunning() && isGameOver == false)
                speedTimer.start();
 
            // سيتم بدأ اللعبة من جديد في حال كان قد تم إيقاف اللعبة لأن اللاعب قد خسر
            else if(!speedTimer.isRunning() && isGameOver == true)
            {
                isGameOver = false;
                speedTimer.start();
                moves = 0;
                totalScore = 0;
                fruitEaten = 0;
                lengthOfSnake = 3;
                right = true;
                left = false;
                xPos = random.nextInt(22);
                yPos = 5+random.nextInt(17);
            }
        }
 
        // هنا قمنا بتحديد ما سيحدث إذا قام اللاعب بالنقر على زر السهم الأيمن
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            // إذا لم تكن الأفعى تسير في الإتجاه الأيسر سيتم توجيهها نحو الإتجاه الأيمن
            moves++;
            right = true;
 
            if(!left)
                right = true;
 
            else
            {
                right = false;
                left = true;
            }
 
            up = false;
            down = false;
        }
 
        // هنا قمنا بتحديد ما سيحدث إذا قام اللاعب بالنقر على زر السهم الأيسر
        else if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            // إذا لم تكن الأفعى تسير في الإتجاه الأيمن سيتم توجيهها نحو الإتجاه الأيسر
            moves++;
            left = true;
 
            if(!right)
                left = true;
 
            else
            {
                left = false;
                right = true;
            }
 
            up = false;
            down = false;
        }
 
        // هنا قمنا بتحديد ما سيحدث إذا قام اللاعب بالنقر على زر السهم المتجه للأعلى
        else if(e.getKeyCode() == KeyEvent.VK_UP)
        {
            // إذا لم تكن الأفعى تسير في اتجاه الأسفل سيتم توجيهها نحو الأعلى
            moves++;
            up = true;
 
            if(!down)
                up = true;
 
            else
            {
                up = false;
                down = true;
            }
 
            left = false;
            right = false;
        }
 
        // هنا قمنا بتحديد ما سيحدث إذا قام اللاعب بالنقر على زر السهم المتجه للأسفل
        else if(e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            // إذا لم تكن الأفعى تسير في اتجاه الأعلى سيتم توجيهها نحو الأسفل
            moves++;
            down = true;
 
            if(!up)
                down = true;
 
            else
            {
                up = true;
                down = false;
            }
 
            left = false;
            right = false;
        }
    }
 
 
    // هنا قمنا بتحديد المكان الذي ستظهر فيه الأفعى لحظة تحركها
    // و لاحظ أنه يوجد شرط متعلق بكل إتجاه تسلكه الأفعى حالياً
    // هذه الشروط الموضوعة تجعل الأفعى تظهر في الجهة المقابلة لها عندما تلمس الحائط
    @Override
    public void actionPerformed(ActionEvent e) {
 
        if(right)
        {
            for(int i = lengthOfSnake-1; i>=0; i--)
                boardY[i+1] = boardY[i];
 
            for(int i = lengthOfSnake; i>=0; i--)
            {
                if(i==0)
                    boardX[i] = boardX[i] + 20;
 
                else
                    boardX[i] = boardX[i-1];
 
                if(boardX[i] > 460)
                    boardX[i] = 20;
            }
        }
 
        else if(left)
        {
            for(int i = lengthOfSnake-1; i>=0; i--)
                boardY[i+1] = boardY[i];
 
            for(int i = lengthOfSnake; i>=0; i--)
            {
                if(i==0)
                    boardX[i] = boardX[i] - 20;
 
                else
                    boardX[i] = boardX[i-1];
 
                if(boardX[i] < 20)
                    boardX[i] = 460;
            }
        }
 
        else if(up)
        {
            for(int i = lengthOfSnake-1; i>=0; i--)
                boardX[i+1] = boardX[i];
 
            for(int i = lengthOfSnake; i>=0; i--)
            {
                if(i==0)
                    boardY[i] = boardY[i] - 20;
 
                else
                    boardY[i] = boardY[i-1];
 
                if(boardY[i] < 20)
                    boardY[i] = 460;
            }
        }
 
        else if(down)
        {
            for(int i = lengthOfSnake-1; i>=0; i--)
                boardX[i+1] = boardX[i];
 
            for(int i = lengthOfSnake; i>=0; i--)
            {
                if(i==0)
                    boardY[i] = boardY[i] + 20;
 
                else
                    boardY[i] = boardY[i-1];
 
                if(boardY[i] > 460)
                    boardY[i] = 20;
            }
        }
 
        repaint();
    }
 
 
    @Override
    public void keyReleased(KeyEvent e) {
 
    }
 
 
    @Override
    public void keyTyped(KeyEvent e) {
 
    }
 
 
    // هذه الدالة تحفظ أعلى مجموع وصل إليه اللاعب في ملف خارجي بجانب ملف اللعبة
    private void writeBestScoreInTheFile()
    {
        if(totalScore >= bestScore)
        {
            try {
                FileOutputStream fos = new FileOutputStream("./snake-game-best-score.txt");
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                osw.write(bestScore+"");
                osw.flush();
                osw.close();
            }
            catch(IOException e) {
            }
        }
    }
 
 
    // هذه الدالة تقرأ أعلى مجموع وصل إليه اللاعب من الملف الخارجي الموجود بجانب ملف اللعبة
    // في حال كان لا يوجد ملف خارجي ستقوم بإنشائه و وضع القيمة 0 فيه كقيمة أولية و هذا ما سيحدث عندما يقوم اللاعب بتشغيل اللعبة أول مرة
    private int readBestScorefromTheFile()
    {
        try {
            InputStreamReader isr = new InputStreamReader( new FileInputStream("./snake-game-best-score.txt"), "UTF-8" );
            BufferedReader br = new BufferedReader(isr);
 
            String str = "";
            int c;
            while( (c = br.read()) != -1){
                if(Character.isDigit(c))
                    str += (char)c;
            }
            if(str.equals(""))
                str = "0";
 
            br.close();
            return Integer.parseInt(str);
        }
        catch(IOException e) {
        }
        return 0;
    }
 
}