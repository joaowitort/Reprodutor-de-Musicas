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
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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

    private PlayerWindow window;

    private int currentFrame = 0;

    private final ActionListener buttonListenerPlayNow = e -> {
        try {
            playNow();
        } catch (JavaLayerException ex) {
            throw new RuntimeException(ex);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    };
    private final ActionListener buttonListenerRemove = e -> {
        removerSong();
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
    };
    private final ActionListener buttonListenerStop = e -> {
    };
    private final ActionListener buttonListenerNext = e -> {
    };
    private final ActionListener buttonListenerPrevious = e -> {
    };
    private final ActionListener buttonListenerShuffle = e -> {
    };
    private final ActionListener buttonListenerLoop = e -> {
    };
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

    private ArmazemSong armSong;

    public Player() {
        this.armSong = new ArmazemSong();
        EventQueue.invokeLater(() -> this.window = new PlayerWindow(
                "CinMusic",
                this.armSong.getListaReproducao(),
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

    //Função responsável por adicionar uma música
    private void adicionarSong() throws InvalidDataException, UnsupportedTagException, IOException, BitstreamException {
        Song a = this.window.openFileChooser();
        this.armSong.addSongList(a);
        this.window.setQueueList(this.armSong.getListaReproducao());
    }

    private void removerSong() {
        String id = this.window.getSelectedSong();
        this.armSong.removeSongList(id);
        this.window.setQueueList(this.armSong.getListaReproducao());
    }

    private void playNow() throws JavaLayerException, FileNotFoundException {
        Song atual = this.armSong.getSong(this.window.getSelectedSong());
        play(atual);
    }

    private void play(Song a) throws FileNotFoundException {
        BufferedInputStream bin = a.getBufferedInputStream();
        new Thread(() -> {
            try {
                this.device = FactoryRegistry.systemRegistry().createAudioDevice();
            } catch (JavaLayerException e) {
                throw new RuntimeException(e);
            }
            try {
                this.device.open(this.decoder = new Decoder());
            } catch (JavaLayerException e) {
                throw new RuntimeException(e);
            }
            try {
                this.bitstream = new Bitstream(a.getBufferedInputStream());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            this.currentFrame = 0;

            while (true){
                try {
                    if (!this.playNextFrame()) break;
                } catch (JavaLayerException e) {
                    throw new RuntimeException(e);
                }
                this.window.setPlayingSongInfo(a.getTitle(),a.getAlbum(),a.getArtist());
            }


        }).start();
    }
    //</editor-fold>

    protected AudioDevice getAudioDevice() throws JavaLayerException {
        return FactoryRegistry.systemRegistry().createAudioDevice();
    }
}


