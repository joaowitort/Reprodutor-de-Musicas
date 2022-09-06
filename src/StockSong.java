import support.Song;

import java.util.ArrayList;

public class StockSong {
    private ArrayList<Song> songList = new ArrayList<>();

    //Creates a copy of the Song object passed in.
    public StockSong() {
    }

    //Add a Song-type object to the ArrayList
    public void addSongList(Song a) {
        songList.add(a);
    }

    //returns a String[][] containing information about the songs
    public String[][] getPlaylist() {
        String[][] playlist = new String[songList.size()][6];
        for (int i = 0; i < songList.size(); i++) {
            playlist[i][0] = songList.get(i).getTitle();
            playlist[i][1] = songList.get(i).getAlbum();
            playlist[i][2] = songList.get(i).getArtist();
            playlist[i][3] = songList.get(i).getYear();
            playlist[i][4] = songList.get(i).getStrLength();
            playlist[i][5] = songList.get(i).getUuid();
        }

        return playlist;
    }

    public void removeSongList(String id) {
        int position = searchPosition(id);
        if (position >= 0) {
            songList.remove(position);
        }
    }

    public Song getSong(String id) {
        int position = searchPosition(id);
        return songList.get(position);
    }

    public int searchPosition(String id) {
        for (int i = 0; i < songList.size(); i++) {
            if (songList.get(i).getUuid() == id) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList setSongsStored() {
        return songList;
    }
}
