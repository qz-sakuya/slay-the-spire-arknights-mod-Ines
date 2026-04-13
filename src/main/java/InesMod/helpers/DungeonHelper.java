package InesMod.helpers;


import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DungeonHelper
{
    List<ArrayList<MapRoomNode>> map = new ArrayList<>();

    public static MapRoomNode createNode(int x, int y) {
        return new MapRoomNode(x, y);
    }

    public static MapRoomNode createNode(int x, int y, AbstractRoom room) {
        MapRoomNode tmp = new MapRoomNode(x, y);
        tmp.room = room;
        return tmp;
    }

    public static void connectNode(MapRoomNode src, MapRoomNode dst) {
        src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, false));
    }

    public static ArrayList<MapRoomNode> createRow(int row, MapRoomNode node) {
        ArrayList<MapRoomNode> tmp = new ArrayList<>();
        tmp.add(node);
        return createRow(row, tmp);
    }

    public static ArrayList<MapRoomNode> createRow(int row, MapRoomNode node1, MapRoomNode node2) {
        ArrayList<MapRoomNode> tmp = new ArrayList<>();
        tmp.add(node1);
        tmp.add(node2);
        return createRow(row, tmp);
    }

    public static ArrayList<MapRoomNode> createRow(int row, MapRoomNode node1, MapRoomNode node2, MapRoomNode node3) {
        ArrayList<MapRoomNode> tmp = new ArrayList<>();
        tmp.add(node1);
        tmp.add(node2);
        tmp.add(node3);
        return createRow(row, tmp);
    }

    public static ArrayList<MapRoomNode> createRow(int row, ArrayList<MapRoomNode> nodes) {
        ArrayList<MapRoomNode> tmp = new ArrayList<>();
        int i;
        // 生成一个包含 7 个 Node 的空白行
        for (i = 0; i < 7; i++) {
            tmp.add(new MapRoomNode(i, row));
        }

        // 根据传入节点的x坐标，放入空白行中
        for (i = 0; i < nodes.size(); i++) {
            tmp.set(nodes.get(i).x, nodes.get(i));
        }


        return tmp;
    }

    public static ArrayList<ArrayList<MapRoomNode>> createMap(MapRoomNode... nodes) {
        ArrayList<MapRoomNode> nodeList = new ArrayList<>(Arrays.asList(nodes));
        return createMap(nodeList);
    }

    public static ArrayList<ArrayList<MapRoomNode>> createMap(ArrayList<MapRoomNode> nodes) {
        ArrayList<ArrayList<MapRoomNode>> map = new ArrayList<>();
        int maxY = 0;
        for (MapRoomNode node : nodes) {
            if (node.y > maxY) {
                maxY = node.y;
            }
        }

        for(int i = 0; i <= maxY; i++) {
            // 生成一个包含 7 个 Node 的空白行
            ArrayList<MapRoomNode> tmpRow = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                tmpRow.add(createNode(j, i));
            }
            map.add(tmpRow);
        }

        // 根据传入节点的x和y坐标，放入对应位置
        for (MapRoomNode node : nodes) {
            ArrayList<MapRoomNode> tmpRow = map.get(node.y);
            tmpRow.set(node.x, node);
        }

        return map;
    }


}
