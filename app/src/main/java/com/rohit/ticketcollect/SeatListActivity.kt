package com.rohit.ticketcollect

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohit.ticketcollect.Adapter.DateAdapter
import com.rohit.ticketcollect.Adapter.SeatListAdapter
import com.rohit.ticketcollect.Adapter.TimeAdapter
import com.rohit.ticketcollect.Models.Film
import com.rohit.ticketcollect.Models.Seat
import com.rohit.ticketcollect.databinding.ActivitySeatListBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class SeatListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeatListBinding
    private lateinit var film : Film
    private var price : Double = 0.0
    private var number : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentExtra()
        setVariable()
        initSeatList()

        binding.downloadBtn.setOnClickListener {
            downloadTicket()
        }
    }

    private fun downloadTicket() {
        // Create a PDF document
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint()
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 16f
        paint.color = Color.BLACK

        // Customize this part to draw your ticket content
        val titlePaint = Paint()
        titlePaint.textAlign = Paint.Align.CENTER
        titlePaint.textSize = 18f
        titlePaint.color = Color.BLACK
        titlePaint.isFakeBoldText = true

        canvas.drawText("Movie Ticket", pageInfo.pageWidth / 2f, 40f, titlePaint)

        val movieName = film.Title // Assuming film.name contains the movie name
        val selectedSeats = "$number seats selected"
        val totalPrice = "$$price"
        val date = "Date: ${generateDates()[0]}"   // Sample date, can be customized
        val time = "Time: ${generateTimeSlots()[0]}"   // Sample time, can be customized

        canvas.drawText("Movie: $movieName", 20f, 100f, paint)
        canvas.drawText("Selected Seats: $selectedSeats", 20f, 140f, paint)
        canvas.drawText("Total Price: $totalPrice", 20f, 180f, paint)
        canvas.drawText(date, 20f, 220f, paint)
        canvas.drawText(time, 20f, 260f, paint)

        // Add any other details you need here

        pdfDocument.finishPage(page)

        // Save PDF to external storage
        val file = File(getExternalFilesDir(null), "Ticket.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(this, "Ticket downloaded to ${file.absolutePath}", Toast.LENGTH_SHORT).show()

            // Open the PDF
            openPdf(file)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to download ticket", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }

    private fun openPdf(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(this, "${packageName}.provider", file)
        intent.setDataAndType(uri, "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No PDF viewer found", Toast.LENGTH_SHORT).show()
        }
    }


    private fun initSeatList() {
        val gridLayoutManager = GridLayoutManager(this,7)
        gridLayoutManager.spanSizeLookup = object :GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return if (position % 7 == 3)1 else 1
            }
        }

        binding.seatRecyclerView.layoutManager = gridLayoutManager

        val seatList = mutableListOf<Seat>()
        val numberSeats = 81

        for (i in 0 until numberSeats){
            val SeatName = ""
            val SeatStatus = if (i==2 || i==20 || i==33 || i==41 || i==50 || i==72 ||i==73)Seat.SeatStatus.UNAVAILABLE else Seat.SeatStatus.AVAILABLE

            seatList.add(Seat(SeatStatus, SeatName))
        }

        val SeatAdapter = SeatListAdapter(seatList,this,object : SeatListAdapter.SelectedSeat{
            override fun Return(selectedName: String, num: Int) {
                binding.numberSelectTxt.text = "$num Seat Selected"
                val df  = DecimalFormat("#.##")
                price = df.format(num * film.price).toDouble()
                number = num
                binding.priceTxt.text = "$price Rs"
            }
        })
        binding.seatRecyclerView.adapter = SeatAdapter
        binding.seatRecyclerView.isNestedScrollingEnabled = false

        binding.TimeRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.TimeRecyclerView.adapter = TimeAdapter(generateTimeSlots())

        binding.dateRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.dateRecyclerView.adapter = DateAdapter(generateDates())
    }

    private fun setVariable() {
        binding.backBtn2.setOnClickListener {
            finish()
        }
    }

    private fun getIntentExtra() {
        film = intent.getParcelableExtra("film")!!
    }

    private fun generateTimeSlots():List<String>{
        val timeSlots = mutableListOf<String>()
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")

        for (i in 0 until 24 step 2){
            val time = LocalTime.of(i,0)
            timeSlots.add(time.format(formatter))
        }
        return timeSlots
    }

    private fun generateDates():List<String>{
        val dates = mutableListOf<String>()
        val today =LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEE/dd/MMM")

        for (i in 0 until 7){
            dates.add(today.plusDays(i.toLong()).format(formatter))
        }
        return dates
    }

}