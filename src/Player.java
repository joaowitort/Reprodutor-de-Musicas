import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javazoom.jl.decoder.*;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import support.PlayerWindow;
import support.Song;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Player {

    /**
     * The MPEG audio bitstream.
     */
    private Bitstream bitstream;
    /**
     * The MPEG audio decoder.
     */
    private Decoder decoder;
    /**
     * The AudioDevice where audio samples are written to.
     */
    private AudioDevice device;

    private PlayerWindow swindow;

    private int currentFrame = 0;

    private final ActionListener buttonListenerPlayNow = e -> {


        new Thread ( ( ) -> {

            if (button == swindow.BUTTON_ICON_PLAY){

                System.out.println("reproduzir a música");
                this.device = FactoryRegistry.systemRegistry().createAudioDevice();
                this.device.open(this.decoder = new Decoder());
                this.bitstream = new Bitstream(currentSong.getBufferedInputStream());


        }).start(playNextFrame);








    };
    private final ActionListener buttonListenerRemove = e ->  {









    };
    private final ActionListener buttonListenerAddSong = e -> {
        try {
            adicionarSong();
        } catch (InvalidDataException ex) {
            throw new RuntimeException(ex);
        } catch (UnsupportedTagException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (BitstreamException ex) {
            throw new RuntimeException(ex);
        }
    };
    private final ActionListener buttonListenerPlayPause = e -> {

        new Thread ( ( ) -> {

            if (button == swindow.BUTTON_ICON_PLAY) {

                System.out.println("play song");
                this.device = FactoryRegistry.systemRegistry().createAudioDevice();
                this.device.open(this.decoder = new Decoder());
                this.bitstream = new Bitstream(currentSong.getBufferedInputStream());


            }else if(button == swindow.BUTTON_ICON_PAUSE){
                System.out.println("pause song");
                bitstream.close();
                device.close();

            }

            }).start();


        };








    };
    private final ActionListener buttonListenerStop = e ->{ };
    private final ActionListener buttonListenerNext = e ->{ };
    private final ActionListener buttonListenerPrevious = e ->{ };
    private final ActionListener buttonListenerShuffle = e -> { };
    private final ActionListener buttonListenerLoop = e -> { };
    private final MouseInputAdapter scrubberMouseInputAdapter = new MouseInputAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }
    };

    // lista para armazenar músicas
    ArrayList<String[]> queueList;
    // identificador para o array
    int identificador;
    //janela
    PlayerWindow window;
    public Player() {
        // inicializando o array
        this.queueList = new ArrayList<>();
        // inicializando o contador
        identificador = 0;
        //só pra conseguir rodar
        String[][] LISTA_DE_REPRODUÇÃO = new String[2][2];

        EventQueue.invokeLater(() -> this.window = new PlayerWindow(
                "CinMusic",
                LISTA_DE_REPRODUÇÃO,
                buttonListenerPlayNow,
                buttonListenerRemove,
                buttonListenerAddSong,
                buttonListenerShuffle,
                buttonListenerPrevious,
                buttonListenerPlayPause,
                buttonListenerStop,
                buttonListenerNext,
                buttonListenerLoop,
                scrubberMouseInputAdapter)
        );
    }

    //<editor-fold desc="Essential">

    /**
     * @return False if there are no more frames to play.
     */
    private boolean playNextFrame() throws JavaLayerException {
        // TODO Is this thread safe?
        if (device != null) {
            Header h = bitstream.readFrame();
            if (h == null) return false;

            SampleBuffer output = (SampleBuffer) decoder.decodeFrame(h, bitstream);
            device.write(output.getBuffer(), 0, output.getBufferLength());
            bitstream.closeFrame();
        }
        return true;
    }

    /**
     * @return False if there are no more frames to skip.
     */
    private boolean skipNextFrame() throws BitstreamException {
        // TODO Is this thread safe?
        Header h = bitstream.readFrame();
        if (h == null) return false;
        bitstream.closeFrame();
        currentFrame++;
        return true;
    }

    /**
     * Skips bitstream to the target frame if the new frame is higher than the current one.
     *
     * @param newFrame Frame to skip to.
     * @throws BitstreamException Generic Bitstream exception.
     */
    private void skipToFrame(int newFrame) throws BitstreamException {
        // TODO Is this thread safe?
        if (newFrame > currentFrame) {
            int framesToSkip = newFrame - currentFrame;
            boolean condition = true;
            while (framesToSkip-- > 0 && condition) condition = skipNextFrame();
        }
    }

    public class GetSong{
        Song addSongWindow;

        public GetSong(){
            this.addSongWindow = null;
        }

        public void setWindow(Song addSongWindow){
            this.addSongWindow = addSongWindow;
        }
    }

    //Função responsável por adicionar uma música
    private void adicionarSong() throws InvalidDataException, UnsupportedTagException, IOException, BitstreamException {
        this.window.openFileChooser();
    }
    //</editor-fold>
}
