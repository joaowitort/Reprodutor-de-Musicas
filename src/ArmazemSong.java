import support.Song;

import java.util.ArrayList;

public class ArmazemSong {
    private ArrayList<Song> songList = new ArrayList<>();

    public ArmazemSong() {
    }

    public void addSongList(Song a) {
        songList.add(a);
    }

    public String[][] setListaReproducao() {
        String[][] listaReproducao = new String[songList.size()][5];
        for (int i = 0; i < songList.size(); i++) {
            listaReproducao[i][0] = songList.get(i).getTitle();
            listaReproducao[i][1] = songList.get(i).getAlbum();
            listaReproducao[i][2] = songList.get(i).getArtist();
            listaReproducao[i][3] = songList.get(i).getYear();
            listaReproducao[i][4] = songList.get(i).getStrLength();
        }

        return listaReproducao;
    }


}
