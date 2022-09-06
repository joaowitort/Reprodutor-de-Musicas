import support.Song;

import java.util.ArrayList;

public class ArmazemSong {
    private ArrayList<Song> songList = new ArrayList<>();

    public ArmazemSong() {
    }

    public void addSongList(Song a) {
        songList.add(a);
    }

    public String[][] getListaReproducao() {
        String[][] listaReproducao = new String[songList.size()][6];
        for (int i = 0; i < songList.size(); i++) {
            listaReproducao[i][0] = songList.get(i).getTitle();
            listaReproducao[i][1] = songList.get(i).getAlbum();
            listaReproducao[i][2] = songList.get(i).getArtist();
            listaReproducao[i][3] = songList.get(i).getYear();
            listaReproducao[i][4] = songList.get(i).getStrLength();
            listaReproducao[i][5] = songList.get(i).getUuid();
        }

        return listaReproducao;
    }

    public void removeSongList(String id) {
        int posicao = buscarPosicao(id);
        if (posicao >= 0) {
            songList.remove(posicao);
        }
    }

    public Song getSong(String id) {
        int posicao = buscarPosicao(id);
        return songList.get(posicao);
    }

    public int buscarPosicao(String id) {
        for (int i = 0; i < songList.size(); i++) {
            if (songList.get(i).getUuid() == id) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList setSongsArmazenados(){
        return  songList;
    }
}
