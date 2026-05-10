package com.Safe.Link.controllers;

import com.Safe.Link.entities.ItemDelKit;
import com.Safe.Link.services.ItemDelKitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itemkit")
public class ItemDelKitController {

    private final ItemDelKitService itemDelKitService;

    public ItemDelKitController(ItemDelKitService itemDelKitService) {
        this.itemDelKitService = itemDelKitService;
    }

    @PostMapping
    public ResponseEntity<ItemDelKit> guardar(@RequestBody ItemDelKit itemDelKit) {
        try {
            ItemDelKit nuevo = itemDelKitService.guardar(itemDelKit);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo); // 201
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // 400
        }
    }

    @GetMapping
    public ResponseEntity<List<ItemDelKit>> listar() {
        try {
            return ResponseEntity.ok(itemDelKitService.listar()); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDelKit> buscarPorId(@PathVariable Long id) {
        try {
            ItemDelKit item = itemDelKitService.buscarPorId(id);

            if (item == null) {
                return ResponseEntity.notFound().build(); // 404
            }

            return ResponseEntity.ok(item); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    @GetMapping("/kit/{idKit}")
    public ResponseEntity<List<ItemDelKit>> listarPorKit(@PathVariable Long idKit) {
        try {
            return ResponseEntity.ok(itemDelKitService.listarPorKit(idKit)); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            ItemDelKit item = itemDelKitService.buscarPorId(id);

            if (item == null) {
                return ResponseEntity.notFound().build(); // 404
            }

            itemDelKitService.eliminar(id);
            return ResponseEntity.ok().build(); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }
}
