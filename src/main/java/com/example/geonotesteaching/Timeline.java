package com.example.geonotesteaching;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
// La clase 'Timeline' usa un 'SequencedMap' para mantener las notas en orden de inserción.
// A diferencia de un HashMap, un 'SequencedMap' garantiza el orden y permite acceder
// al primer y último elemento de forma eficiente.
final class Timeline {
    private final Map<Long, Note> notes = new LinkedHashMap<>();

    public void addNote(Note note) { 
        notes.put(note.id(), note); 
    }

    public Note getNote(long id) { 
        return notes.get(id); 
    }

    public Map<Long, Note> getNotes() { 
        return notes; 
    }

    public List<Note> latest(int num) {
        Set<Note> ordenedNotes = new TreeSet<>(new ComparadorPorCreatedAt());
        ordenedNotes.addAll(notes.values());

        List<Note> listaDevuelta = new ArrayList<>();
        
        int cont = 0;

        for (Note note : ordenedNotes) {
            if (cont == num)
                break;

            cont ++;

            listaDevuelta.add(note);
        }
        
        return listaDevuelta;
    }

    // Esta clase final genera la salida JSON usando 'text blocks'.
    public final class Render extends AbstractExporter implements Exporter {
        @Override 
        public String export() {
            var notesList = notes.values().stream()
                // Un 'text block' es una cadena de texto multilinea que no necesita
                // concatenación ni caracteres de escape para las comillas.
                .map(note -> """
                        {
                            "id": %d,
                            "title": "%s",
                            "content": "%s",
                            "location": { 
                                "lat": %f, 
                                "lon": %f
                            },
                            "createdAt": "%s"
                        }
                        """.formatted(
                            note.id(), note.title(), note.content().replace("\"", "\\\""),
                            note.location().lat(), note.location().lon(),
                            note.createdAt()))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.joining(",\n"));
            return """
                    { "notes": [ %s ] }
                    """.formatted(notesList);
        }
    }
}